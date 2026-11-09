package com.spring.ApiSystem.shared.infrastructure.telemetry;

import com.spring.ApiSystem.domain.usuario.Usuario;
import com.spring.ApiSystem.domain.usuario.security.JpaUserDetailsService;
import com.spring.ApiSystem.shared.exception.CustomApiException;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@ConditionalOnProperty(prefix = "observability", name = "enabled", havingValue = "true", matchIfMissing = true)
public class MobileLogIngestionService {

    private static final Logger mobileLog = LoggerFactory.getLogger("com.spring.ApiSystem.mobile.telemetry");

    public static final int MAX_ENTRIES_PER_BATCH = 50;
    public static final int MAX_MESSAGE_BYTES = 8 * 1024;

    private final MobileLogRateLimiter rateLimiter;
    private JpaUserDetailsService userDetails;

    public MobileLogIngestionService(MobileLogRateLimiter rateLimiter) {
        this.rateLimiter = rateLimiter;
    }

    @Autowired
    public MobileLogIngestionService(
            MobileLogRateLimiter rateLimiter,
            JpaUserDetailsService userDetails
    ) {
        this.rateLimiter = rateLimiter;
        this.userDetails = userDetails;
    }

    public int ingest(MobileLogBatchDTO batch, HttpServletRequest request) {
        if (!rateLimiter.tryConsume(request)) {
            throw new CustomApiException(
                    "Limite de requisições excedido para este IP (máx. 60 por minuto).",
                    HttpStatus.TOO_MANY_REQUESTS
            );
        }

        if (batch == null || batch.entries() == null || batch.entries().isEmpty()) {
            throw new CustomApiException(
                    "O lote de logs não pode estar vazio.",
                    HttpStatus.BAD_REQUEST
            );
        }

        List<MobileLogEntryDTO> entries = batch.entries();
        if (entries.size() > MAX_ENTRIES_PER_BATCH) {
            throw new CustomApiException(
                    "O tamanho do lote excede o limite máximo de " + MAX_ENTRIES_PER_BATCH + " entradas.",
                    HttpStatus.BAD_REQUEST
            );
        }

        String batchOs = TelemetryMdcFilter.sanitizeOs(
                batch.os() != null ? batch.os() : request.getHeader(TelemetryMdcFilter.HEADER_OS)
        );
        String batchOsVersion = TelemetryMdcFilter.sanitizeHeaderValue(
                batch.osVersion() != null ? batch.osVersion() : request.getHeader(TelemetryMdcFilter.HEADER_OS_VERSION)
        );
        String batchAppVersion = TelemetryMdcFilter.sanitizeHeaderValue(
                batch.appVersion() != null ? batch.appVersion() : request.getHeader(TelemetryMdcFilter.HEADER_APP_VERSION)
        );
        String batchAppBuild = TelemetryMdcFilter.sanitizeHeaderValue(
                batch.appBuild() != null ? batch.appBuild() : request.getHeader(TelemetryMdcFilter.HEADER_APP_BUILD)
        );
        String authenticatedUserId = resolveAuthenticatedUserId();

        Map<String, String> previousMdc = MDC.getCopyOfContextMap();
        int ingestedCount = 0;

        try {
            for (MobileLogEntryDTO entry : entries) {
                if (entry == null) {
                    continue;
                }
                emitEntry(entry, batchOs, batchOsVersion, batchAppVersion, batchAppBuild, authenticatedUserId);
                ingestedCount++;
            }
        } finally {
            if (previousMdc != null) {
                MDC.setContextMap(previousMdc);
            } else {
                MDC.clear();
            }
        }

        return ingestedCount;
    }

    private void emitEntry(
            MobileLogEntryDTO entry,
            String os,
            String osVersion,
            String appVersion,
            String appBuild,
            String userId
    ) {
        MDC.put(TelemetryMdcFilter.MDC_SOURCE, "mobile");
        MDC.put(TelemetryMdcFilter.MDC_OS, os);
        MDC.put(TelemetryMdcFilter.MDC_OS_VERSION, osVersion);
        MDC.put(TelemetryMdcFilter.MDC_APP_VERSION, appVersion);
        MDC.put(TelemetryMdcFilter.MDC_APP_BUILD, appBuild);
        if (userId != null && !userId.isBlank()) {
            MDC.put(TelemetryMdcFilter.MDC_USER_ID, userId);
        }

        String tag = entry.tag() != null ? truncate(entry.tag(), 64) : "MOBILE_LOG";
        MDC.put("tag", tag);

        String message = entry.message() != null ? truncate(entry.message(), MAX_MESSAGE_BYTES) : "";
        String stack = entry.stack() != null ? truncate(entry.stack(), MAX_MESSAGE_BYTES) : "";
        if (!stack.isEmpty()) {
            MDC.put("stack", stack);
        } else {
            MDC.remove("stack");
        }

        Map<String, Object> context = entry.context();
        if (context != null) {
            putContextField(context, "screen", "screen");
            putContextField(context, "url", TelemetryMdcFilter.MDC_URI);
            putContextField(context, "method", TelemetryMdcFilter.MDC_METHOD);
            putContextField(context, "status", TelemetryMdcFilter.MDC_STATUS);
            putContextField(context, "durationMs", TelemetryMdcFilter.MDC_DURATION_MS);
            putContextField(context, "requestId", TelemetryMdcFilter.MDC_REQUEST_ID);
        }

        String formattedMessage = stack.isEmpty()
                ? String.format("[%s] %s", tag, message)
                : String.format("[%s] %s | stack=%s", tag, message, stack);

        String level = entry.level() != null ? entry.level().trim().toUpperCase() : "ERROR";
        switch (level) {
            case "WARN", "WARNING" -> mobileLog.warn(formattedMessage);
            case "INFO" -> mobileLog.info(formattedMessage);
            default -> mobileLog.error(formattedMessage);
        }
    }

    private void putContextField(Map<String, Object> context, String contextKey, String mdcKey) {
        Object value = context.get(contextKey);
        if (value != null) {
            MDC.put(mdcKey, truncate(String.valueOf(value), 256));
        }
    }

    private String resolveAuthenticatedUserId() {
        String mdcUserId = MDC.get(TelemetryMdcFilter.MDC_USER_ID);
        if (mdcUserId != null && !mdcUserId.isBlank()) {
            return mdcUserId;
        }
        if (userDetails != null) {
            return userDetails.isLogged()
                    .map(Usuario::getId)
                    .map(String::valueOf)
                    .orElse(null);
        }
        return null;
    }

    private static String truncate(String input, int maxChars) {
        if (input == null) {
            return "";
        }
        return input.length() <= maxChars ? input : input.substring(0, maxChars);
    }
}
