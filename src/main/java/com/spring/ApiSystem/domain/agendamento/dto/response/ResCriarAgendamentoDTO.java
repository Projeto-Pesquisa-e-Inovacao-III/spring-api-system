package com.spring.ApiSystem.domain.agendamento.dto.response;

import com.spring.ApiSystem.domain.agendamento.enums.AgendamentoStatus;
import com.spring.ApiSystem.shared.enums.DiaSemana;

import java.time.LocalDateTime;

public record ResCriarAgendamentoDTO(
        Long id,
        LocalDateTime data,
        LocalDateTime dataFim,
        DiaSemana diaSemana,
        AgendamentoStatus status,
        String descricao,
        String alunoNome,
        String personalNome,
        String produtoContratadoNome
) {}

