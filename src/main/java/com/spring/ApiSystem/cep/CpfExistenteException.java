package com.spring.ApiSystem.cep;

public class CpfExistenteException extends RuntimeException {
    public CpfExistenteException() {
        super("CPF existente.");
    }
}
