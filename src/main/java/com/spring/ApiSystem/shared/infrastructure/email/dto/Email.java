package com.spring.ApiSystem.notificacoes.email.dto;

public record Email(
        String destinatario,
        String assunto,
        String corpo) {
}
