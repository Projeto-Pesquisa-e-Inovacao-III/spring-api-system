package com.spring.ApiSystem.domain.usuario.exception;

public class EmailExistenteException extends RuntimeException {
    public EmailExistenteException() {
        super("Email existente");
    }
}
