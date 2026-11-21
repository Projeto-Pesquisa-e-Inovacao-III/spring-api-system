package com.spring.ApiSystem.shared.infrastructure.telemetry;

import io.micrometer.core.instrument.Meter;
import io.micrometer.core.instrument.Tag;
import io.micrometer.core.instrument.Tags;
import io.micrometer.core.instrument.config.MeterFilter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.MDC;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Configuration
@ConditionalOnProperty(prefix = "observability", name = "enabled", havingValue = "true", matchIfMissing = true)
public class PrometheusTelemetryObservationCustomizer {

    @Bean
    @Order(0)
    public OncePerRequestFilter telemetryUserIdMdcFilter(TelemetryMdcFilter telemetryMdcFilter) {
        return new OncePerRequestFilter() {
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
                telemetryMdcFilter.populateUserIdInMdc();
                filterChain.doFilter(request, response);
            }
        };
    }

    @Bean
    public MeterFilter telemetryMdcMeterFilter() {
        return new MeterFilter() {
            @Override
            public Meter.Id map(Meter.Id id) {
                if (!id.getName().startsWith("http.server.requests")) {
                    return id;
                }

                String os = MDC.get(TelemetryMdcFilter.MDC_OS);
                String appVersion = MDC.get(TelemetryMdcFilter.MDC_APP_VERSION);

                String safeOs = (os != null && !os.isBlank()) ? os : "unknown";
                String safeAppVersion = (appVersion != null && !appVersion.isBlank()) ? appVersion : "unknown";

                return id.withTags(Tags.of(
                        Tag.of("os", safeOs),
                        Tag.of("app_version", safeAppVersion)
                ));
            }
        };
    }
}
