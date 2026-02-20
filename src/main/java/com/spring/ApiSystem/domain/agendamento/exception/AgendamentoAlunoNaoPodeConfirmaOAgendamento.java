package com.spring.ApiSystem.agendamento.exception;

public class AgendamentoAlunoNaoPodeConfirmaOAgendamento extends RuntimeException {
    public AgendamentoAlunoNaoPodeConfirmaOAgendamento() {
        super("Aguardando o personal confirmar o agendamento.");
    }
}
