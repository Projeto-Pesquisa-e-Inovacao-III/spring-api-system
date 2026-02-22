package com.spring.ApiSystem.domain.personal.exception;

public class CrefExistenteException extends RuntimeException {
    public CrefExistenteException() {
        super("CREF em uso");
    }
}
