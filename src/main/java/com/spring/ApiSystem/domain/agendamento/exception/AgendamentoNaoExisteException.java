package com.spring.ApiSystem.agendamento.exception;

public class AgendamentoNaoExisteException extends RuntimeException {
    public AgendamentoNaoExisteException() {
        super("Agendamento  não encontrado.");
    }
}
