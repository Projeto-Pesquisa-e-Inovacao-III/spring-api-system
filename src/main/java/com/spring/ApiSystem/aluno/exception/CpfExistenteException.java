package com.spring.ApiSystem.aluno.exception;

public class CpfExistenteException extends RuntimeException {
    public CpfExistenteException() {
        super("CPF em uso");
    }
}
