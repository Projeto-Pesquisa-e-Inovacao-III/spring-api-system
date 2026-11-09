package com.spring.ApiSystem.shared.infrastructure.telemetry;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.spring.ApiSystem.domain.usuario.Usuario;
import com.spring.ApiSystem.domain.usuario.security.JpaUserDetailsService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
@ConditionalOnProperty(prefix = "observability", name = "enabled", havingValue = "true", matchIfMissing = true)
public class TelemetryMdcFilter extends OncePerRequestFilter {

    private static final Logger accessLog = LoggerFactory.getLogger("com.spring.ApiSystem.access");

    public static final String HEADER_OS = "X-OS";
    public static final String HEADER_OS_VERSION = "X-OS-Version";
    public static final String HEADER_APP_VERSION = "X-App-Version";
    public static final String HEADER_APP_BUILD = "X-App-Build";
    public static final String HEADER_REQUEST_ID = "X-Request-Id";

    public static final String MDC_REQUEST_ID = "requestId";
    public static final String MDC_OS = "os";
    public static final String MDC_OS_VERSION = "osVersion";
    public static final String MDC_APP_VERSION = "appVersion";
    public static final String MDC_APP_BUILD = "appBuild";
    public static final String MDC_USER_ID = "userId";
    public static final String MDC_METHOD = "method";
    public static final String MDC_URI = "uri";
    public static final String MDC_STATUS = "status";
    public static final String MDC_DURATION_MS = "durationMs";
    public static final String MDC_SOURCE = "source";

    private static final int MAX_HEADER_LENGTH = 32;
    private static final int MAX_REQUEST_ID_LENGTH = 64;
    private static final Pattern SAFE_VALUE_PATTERN = Pattern.compile("^[a-zA-Z0-9._-]+$");
    private static final Set<String> ALLOWED_OS = Set.of("android", "ios", "web", "unknown");

    private final Cache<String, String> userIdCache = Caffeine.newBuilder()
            .expireAfterAccess(30, TimeUnit.MINUTES)
            .maximumSize(10_000)
            .build();

    private JpaUserDetailsService userDetails;

    public TelemetryMdcFilter() {
    }

    @Autowired
    public TelemetryMdcFilter(JpaUserDetailsService userDetails) {
        this.userDetails = userDetails;
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String uri = request.getRequestURI();
        return uri != null && !uri.startsWith("/api/");
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {
        long startNanos = System.nanoTime();

        String os = sanitizeOs(resolveHeaderOrParam(request, HEADER_OS));
        String osVersion = sanitizeHeaderValue(resolveHeaderOrParam(request, HEADER_OS_VERSION));
        String appVersion = sanitizeHeaderValue(resolveHeaderOrParam(request, HEADER_APP_VERSION));
        String appBuild = sanitizeHeaderValue(resolveHeaderOrParam(request, HEADER_APP_BUILD));
        String requestId = resolveOrGenerateRequestId(request.getHeader(HEADER_REQUEST_ID));
        String method = request.getMethod();
        String uri = request.getRequestURI();

        MDC.put(MDC_REQUEST_ID, requestId);
        MDC.put(MDC_OS, os);
        MDC.put(MDC_OS_VERSION, osVersion);
        MDC.put(MDC_APP_VERSION, appVersion);
        MDC.put(MDC_APP_BUILD, appBuild);
        MDC.put(MDC_METHOD, method);
        MDC.put(MDC_URI, uri);
        MDC.put(MDC_SOURCE, "backend");

        response.setHeader(HEADER_REQUEST_ID, requestId);

        try {
            filterChain.doFilter(request, response);

            if (MDC.get(MDC_USER_ID) == null) {
                populateUserIdInMdc();
            }

            if (accessLog.isInfoEnabled()) {
                long durationMs = (System.nanoTime() - startNanos) / 1_000_000L;
                int status = response.getStatus();

                MDC.put(MDC_STATUS, Integer.toString(status));
                MDC.put(MDC_DURATION_MS, Long.toString(durationMs));

                accessLog.info(
                        "HTTP {} {} status={} durationMs={} os={} appVersion={} requestId={}",
                        method,
                        uri,
                        status,
                        durationMs,
                        os,
                        appVersion,
                        requestId
                );
            }
        } finally {
            MDC.clear();
        }
    }

    private static String resolveHeaderOrParam(HttpServletRequest request, String name) {
        String header = request.getHeader(name);
        return header != null ? header : request.getParameter(name);
    }

    public static String sanitizeOs(String rawOs) {
        if (rawOs == null || rawOs.isBlank()) {
            return "unknown";
        }
        String normalized = rawOs.trim().toLowerCase();
        if (normalized.length() > MAX_HEADER_LENGTH || !ALLOWED_OS.contains(normalized)) {
            return "unknown";
        }
        return normalized;
    }

    public static String sanitizeHeaderValue(String rawValue) {
        if (rawValue == null || rawValue.isBlank()) {
            return "unknown";
        }
        String trimmed = rawValue.trim();
        if (trimmed.length() > MAX_HEADER_LENGTH || !SAFE_VALUE_PATTERN.matcher(trimmed).matches()) {
            return "unknown";
        }
        return trimmed;
    }

    public static String resolveOrGenerateRequestId(String rawRequestId) {
        if (rawRequestId != null && !rawRequestId.isBlank()) {
            String trimmed = rawRequestId.trim();
            if (trimmed.length() <= MAX_REQUEST_ID_LENGTH && SAFE_VALUE_PATTERN.matcher(trimmed).matches()) {
                return trimmed;
            }
        }
        return UUID.randomUUID().toString();
    }

    public void populateUserIdInMdc() {
        String userId = resolveCachedUserId();
        if (userId != null) {
            MDC.put(MDC_USER_ID, userId);
        }
    }

    public String resolveCachedUserId() {
        if (userDetails == null) {
            return null;
        }
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated() && !"anonymousUser".equals(auth.getPrincipal())) {
            String key = auth.getName();
            if (key != null && !key.isEmpty()) {
                return userIdCache.get(key, k -> fetchUserIdFromService());
            }
        }
        return fetchUserIdFromService();
    }

    private String fetchUserIdFromService() {
        return userDetails.isLogged()
                .map(Usuario::getId)
                .map(String::valueOf)
                .orElse(null);
    }
}
