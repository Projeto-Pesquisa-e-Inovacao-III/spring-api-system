package com.spring.ApiSystem.usuario.exception;

public class EmailExistenteException extends RuntimeException {
    public EmailExistenteException() {
        super("Email existente");
    }
}
