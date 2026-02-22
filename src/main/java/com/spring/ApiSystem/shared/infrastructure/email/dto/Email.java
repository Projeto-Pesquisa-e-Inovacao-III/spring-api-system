package com.spring.ApiSystem.shared.infrastructure.email.dto;

public record Email(
        String destinatario,
        String assunto,
        String corpo) {
}
