package com.spring.ApiSystem.domain.usuario.exception;

public class CpfExistenteException extends RuntimeException {
    public CpfExistenteException() {
        super("CPF existente.");
    }
}
