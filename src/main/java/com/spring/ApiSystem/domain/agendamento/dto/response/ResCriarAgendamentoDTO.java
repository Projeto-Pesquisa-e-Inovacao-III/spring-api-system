package com.spring.ApiSystem.domain.agendamento.dto.response;

import com.spring.ApiSystem.domain.agendamento.enums.AgendamentoStatus;

import java.time.LocalDateTime;

public record ResCriarAgendamentoDTO(
        Long id,
        LocalDateTime data,
        LocalDateTime dataFim,
        AgendamentoStatus status,
        String descricao,
        String alunoNome,
        String personalNome,
        String produtoContratadoNome
) {}

