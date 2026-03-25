package com.spring.ApiSystem.external.comprar.dto.response;

public record LinkDto(
        String id,
        String status,
        String createdAt,
        String payLink
) { }
