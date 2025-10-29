package com.spring.ApiSystem.shared.exception;

public class EmailExistenteException extends RuntimeException {
    public EmailExistenteException() {
        super("Email existente");
    }
}
