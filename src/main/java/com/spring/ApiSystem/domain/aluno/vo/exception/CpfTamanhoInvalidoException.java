package com.spring.ApiSystem.domain.aluno.vo.exception;


public class CpfTamanhoInvalidoException extends IllegalArgumentException {

    public CpfTamanhoInvalidoException() {
        super("CPF deve conter 11 dígitos.");
    }

    public CpfTamanhoInvalidoException(String message) {
        super(message);
    }
}

