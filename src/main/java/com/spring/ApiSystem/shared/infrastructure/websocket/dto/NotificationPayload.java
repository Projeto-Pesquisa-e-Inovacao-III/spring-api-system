package com.spring.ApiSystem.shared.infrastructure.websocket.dto;

import java.time.Instant;

public record NotificationPayload(
        Long id,
        String title,
        String message,
        String type,
        String dadosAdicionais,
        Instant createdAt
) {
    public NotificationPayload(Long id, String title, String message, String type, Instant createdAt) {
        this(id, title, message, type, null, createdAt);
    }

    public NotificationPayload(String title, String message, String type, Instant createdAt) {
        this(null, title, message, type, null, createdAt);
    }
}
