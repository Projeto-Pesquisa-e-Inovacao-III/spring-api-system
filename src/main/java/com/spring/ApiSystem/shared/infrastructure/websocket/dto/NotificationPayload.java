package com.spring.ApiSystem.shared.infrastructure.websocket.dto;

import java.time.Instant;

public record NotificationPayload(
        String title,
        String message,
        String type,
        Instant createdAt
) {
}
