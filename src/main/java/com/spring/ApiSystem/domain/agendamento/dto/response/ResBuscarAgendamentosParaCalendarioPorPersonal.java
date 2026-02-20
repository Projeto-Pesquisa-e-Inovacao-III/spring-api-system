package com.spring.ApiSystem.agendamento.dto.response;

import com.spring.ApiSystem.agendamento.enums.AgendamentoStatus;

import java.time.LocalDateTime;

public record ResBuscarAgendamentosParaCalendarioPorPersonal(
        Long agendamentoId,
        LocalDateTime data,
        AgendamentoStatus status

){ }
