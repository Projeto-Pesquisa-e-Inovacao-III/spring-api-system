package com.spring.ApiSystem.usuario.exception;

public class PersonalTemAcessoApenasException extends RuntimeException {
    public PersonalTemAcessoApenasException() {
        super("Apenas personais podem acessar este recurso.");
    }
}
