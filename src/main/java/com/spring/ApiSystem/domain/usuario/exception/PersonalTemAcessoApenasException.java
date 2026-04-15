package com.spring.ApiSystem.domain.usuario.exception;

public class PersonalTemAcessoApenasException extends RuntimeException {
    public PersonalTemAcessoApenasException() {
        super("Apenas personais podem acessar este recurso.");
    }
}
