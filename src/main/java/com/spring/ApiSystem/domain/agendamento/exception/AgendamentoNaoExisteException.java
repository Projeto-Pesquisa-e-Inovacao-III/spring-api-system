package com.spring.ApiSystem.domain.agendamento.exception;

public class AgendamentoNaoExisteException extends RuntimeException {
    public AgendamentoNaoExisteException() {
        super("Agendamento  não encontrado.");
    }
}
