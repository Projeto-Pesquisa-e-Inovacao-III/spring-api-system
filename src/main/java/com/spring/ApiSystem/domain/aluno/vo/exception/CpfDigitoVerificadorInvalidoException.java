package com.spring.ApiSystem.domain.aluno.vo.exception;


public class CpfDigitoVerificadorInvalidoException extends IllegalArgumentException {

    public CpfDigitoVerificadorInvalidoException() {
        super("CPF inválido.");
    }

    public CpfDigitoVerificadorInvalidoException(String message) {
        super(message);
    }
}

