package com.spring.ApiSystem.domain.usuario.exception;

public class EmailExistenteException extends RuntimeException {
    public EmailExistenteException() {
        super("Usuário com esse e-mail já é existente");
    }
}
