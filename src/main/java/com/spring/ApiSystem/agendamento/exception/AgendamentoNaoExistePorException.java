package com.spring.ApiSystem.agendamento.exception;

public class AgendamentoNaoExistePorException extends RuntimeException {
    public AgendamentoNaoExistePorException() {
        super("Não existe agendamento  não encontrado.");
    }
}
