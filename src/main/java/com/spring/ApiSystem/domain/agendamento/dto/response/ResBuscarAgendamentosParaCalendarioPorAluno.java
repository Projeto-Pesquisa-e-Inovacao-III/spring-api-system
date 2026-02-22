package com.spring.ApiSystem.domain.agendamento.dto.response;


import com.spring.ApiSystem.domain.agendamento.enums.AgendamentoStatus;

import java.time.LocalDateTime;

public record ResBuscarAgendamentosParaCalendarioPorAluno(
    Long agendamentoId,
    LocalDateTime data,
    AgendamentoStatus status
){ }
