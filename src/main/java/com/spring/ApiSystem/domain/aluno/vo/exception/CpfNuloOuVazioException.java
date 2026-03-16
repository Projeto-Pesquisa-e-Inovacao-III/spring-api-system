package com.spring.ApiSystem.domain.aluno.vo.exception;


public class CpfNuloOuVazioException extends IllegalArgumentException {

    public CpfNuloOuVazioException() {
        super("CPF é obrigatório.");
    }

    public CpfNuloOuVazioException(String message) {
        super(message);
    }
}

