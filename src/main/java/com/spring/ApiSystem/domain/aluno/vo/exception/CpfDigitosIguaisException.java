package com.spring.ApiSystem.domain.aluno.vo.exception;


public class CpfDigitosIguaisException extends IllegalArgumentException {

    public CpfDigitosIguaisException() {
        super("CPF inválido.");
    }

    public CpfDigitosIguaisException(String message) {
        super(message);
    }
}

