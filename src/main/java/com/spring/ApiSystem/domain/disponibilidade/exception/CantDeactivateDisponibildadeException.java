package com.spring.ApiSystem.domain.disponibilidade.exception;

import com.spring.ApiSystem.domain.agendamento.Agendamento;
import com.spring.ApiSystem.domain.agendamento.dto.response.ResAgendamentoDataAndNameDto;


import java.util.List;

public class CantDeactivateDisponibildadeException extends RuntimeException {
    private final List<Agendamento> agendamentoList;

    public CantDeactivateDisponibildadeException(List<Agendamento> agendamentoList) {
        super("Existem agendamentos para este dia da semana");
        this.agendamentoList = agendamentoList;
    }

    public List<ResAgendamentoDataAndNameDto> getAgendamentoList() {
        return agendamentoList.stream()
                .map(a -> new ResAgendamentoDataAndNameDto(
                        a.getData().toLocalDate(),   // ou outro campo de data
                        a.getAluno().getNome()
                ))
                .toList();
    }

}
