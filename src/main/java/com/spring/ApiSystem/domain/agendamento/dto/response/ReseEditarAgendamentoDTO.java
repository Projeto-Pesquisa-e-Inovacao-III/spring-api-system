package com.spring.ApiSystem.agendamento.dto.response;

import com.spring.ApiSystem.agendamento.enums.AgendamentoStatus;

import java.time.LocalDateTime;

public record ReseEditarAgendamentoDTO(
        Long id,
        LocalDateTime data,
        AgendamentoStatus status,
        String descricao,
        Long enderecoId,
        Long alunoId,
        Long personalId,
        Long produtoContratadoId
) {}

