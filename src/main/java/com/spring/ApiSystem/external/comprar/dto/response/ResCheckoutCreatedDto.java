package com.spring.ApiSystem.external.comprar.dto.response;

import java.time.OffsetDateTime;

public record ResCheckoutCreatedDto(
        String id,
        String status,
        OffsetDateTime createdAt,
        String payLink
) {
}

