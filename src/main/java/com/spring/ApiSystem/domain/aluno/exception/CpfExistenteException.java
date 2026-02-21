package com.spring.ApiSystem.domain.aluno.exception;

public class CpfExistenteException extends RuntimeException {
    public CpfExistenteException() {
        super("CPF em uso");
    }
}
