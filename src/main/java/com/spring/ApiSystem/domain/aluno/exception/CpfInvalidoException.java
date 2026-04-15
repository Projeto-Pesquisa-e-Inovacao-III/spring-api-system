package com.spring.ApiSystem.domain.aluno.exception;

public class CpfInvalidoException extends RuntimeException {
    public CpfInvalidoException() {
        super("CPF inválido" );
    }
    public CpfInvalidoException(String message) {super( message );
    }

}
