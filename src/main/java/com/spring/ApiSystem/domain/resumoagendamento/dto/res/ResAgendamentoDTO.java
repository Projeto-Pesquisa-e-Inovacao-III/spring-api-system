package com.spring.ApiSystem.domain.resumoagendamento.dto.res;

import com.spring.ApiSystem.domain.agendamento.Agendamento;
import com.spring.ApiSystem.domain.agendamento.enums.AgendamentoStatus;
import com.spring.ApiSystem.shared.enums.DiaSemana;

import java.time.LocalDateTime;

public record ResAgendamentoDTO(
        Long id,
        LocalDateTime data,
        LocalDateTime dataFim,
        DiaSemana diaSemana,
        AgendamentoStatus status,
        String descricao
) {
    public static ResAgendamentoDTO from(Agendamento agendamento) {
        return new ResAgendamentoDTO(
                agendamento.getId(),
                agendamento.getData(),
                agendamento.getDataFim(),
                agendamento.getDiaSemana(),
                agendamento.getStatus(),
                agendamento.getDescricao()
        );
    }
}
