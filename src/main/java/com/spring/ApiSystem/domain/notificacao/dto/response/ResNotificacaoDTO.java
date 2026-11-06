package com.spring.ApiSystem.domain.notificacao.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.spring.ApiSystem.domain.notificacao.Notificacao;

import java.time.Instant;

public record ResNotificacaoDTO(
        Long id,
        String titulo,
        String mensagem,
        String tipo,
        String dadosAdicionais,
        boolean lida,
        Instant createdAt,
        Instant readAt
) {
    public static ResNotificacaoDTO fromEntity(Notificacao entity) {
        return new ResNotificacaoDTO(
                entity.getId(),
                entity.getTitulo(),
                entity.getMensagem(),
                entity.getTipo(),
                entity.getDadosAdicionais(),
                entity.isLida(),
                entity.getCreatedAt(),
                entity.getReadAt()
        );
    }

    /**
     * Alias for frontend compatibility where metadata may be accessed via 'data'.
     * Returns the serialized JSON metadata (containing e.g. "recipient": "aluno" | "personal").
     */
    @JsonProperty("data")
    public String data() {
        return dadosAdicionais;
    }
}
