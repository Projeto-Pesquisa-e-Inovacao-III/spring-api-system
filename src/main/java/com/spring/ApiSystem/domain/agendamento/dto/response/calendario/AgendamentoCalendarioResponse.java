package com.spring.ApiSystem.domain.agendamento.dto.response.calendario;

import com.spring.ApiSystem.domain.agendamento.enums.AgendamentoStatus;

import java.time.LocalDateTime;

public interface AgendamentoCalendarioResponse {
    Long agendamentoId();
    LocalDateTime data();
    AgendamentoStatus status();
}