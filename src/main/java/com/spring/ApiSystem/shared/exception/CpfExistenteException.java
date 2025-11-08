package com.spring.ApiSystem.shared.exception;

public class CpfExistenteException extends RuntimeException {
    public CpfExistenteException() {
        super("CPF existente.");
    }
}
