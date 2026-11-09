package com.spring.ApiSystem.shared.infrastructure.telemetry;

import java.util.Map;

public record MobileLogEntryDTO(
        String level,
        String tag,
        String message,
        String stack,
        String timestamp,
        Map<String, Object> context
) {
}
