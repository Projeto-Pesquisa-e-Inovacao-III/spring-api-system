package com.spring.ApiSystem.domain.recomendacaotreino.exception;

import com.spring.ApiSystem.domain.agendamento.enums.AgendamentoStatus;

public class StatusAgendamentoInvalido extends RuntimeException {
    public StatusAgendamentoInvalido(AgendamentoStatus status) {
        super("Status inválido, o agendamento DEVE ser " + status);
    }
}
