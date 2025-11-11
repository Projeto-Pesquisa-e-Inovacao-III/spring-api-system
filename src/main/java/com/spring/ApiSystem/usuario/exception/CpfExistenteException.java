package com.spring.ApiSystem.usuario.exception;

public class CpfExistenteException extends RuntimeException {
    public CpfExistenteException() {
        super("CPF existente.");
    }
}
