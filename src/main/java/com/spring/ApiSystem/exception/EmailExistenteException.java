package com.spring.ApiSystem.exception;

public class EmailExistenteException extends RuntimeException {
    public EmailExistenteException() {
        super("Email existente");
    }
}
