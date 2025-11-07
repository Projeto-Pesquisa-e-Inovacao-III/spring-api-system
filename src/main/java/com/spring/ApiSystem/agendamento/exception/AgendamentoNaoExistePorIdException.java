package com.spring.ApiSystem.agendamento.exception;

public class AgendamentoNaoExistePorIdException extends RuntimeException {
    public AgendamentoNaoExistePorIdException() {
        super("Não existe agendamento com o ID informado.");
    }
}
