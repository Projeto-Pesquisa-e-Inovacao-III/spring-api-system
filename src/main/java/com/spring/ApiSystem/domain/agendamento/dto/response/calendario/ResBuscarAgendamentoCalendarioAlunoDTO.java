package com.spring.ApiSystem.domain.agendamento.dto.response.calendario;

import com.spring.ApiSystem.domain.agendamento.dto.response.calendario.AgendamentoCalendarioResponse;
import com.spring.ApiSystem.domain.agendamento.enums.AgendamentoStatus;

import java.time.LocalDateTime;

public record ResBuscarAgendamentoCalendarioAlunoDTO(
        Long agendamentoId,
        LocalDateTime data,
        AgendamentoStatus status
) implements AgendamentoCalendarioResponse {
}