package com.spring.ApiSystem.agendamento.dto.response;

import com.spring.ApiSystem.agendamento.enums.AgendamentoStatus;


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

