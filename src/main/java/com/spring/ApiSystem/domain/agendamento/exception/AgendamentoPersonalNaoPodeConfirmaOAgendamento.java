package com.spring.ApiSystem.domain.agendamento.exception;

public class AgendamentoPersonalNaoPodeConfirmaOAgendamento extends RuntimeException {
    public AgendamentoPersonalNaoPodeConfirmaOAgendamento() {
        super("Aguardando o cliente confirmar o agendamento.");
    }
}
