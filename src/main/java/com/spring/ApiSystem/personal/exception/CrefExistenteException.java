package com.spring.ApiSystem.personal.exception;

public class CrefExistenteException extends RuntimeException {
    public CrefExistenteException() {
        super("CREF em uso");
    }
}
