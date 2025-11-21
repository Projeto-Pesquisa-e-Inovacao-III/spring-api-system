package com.spring.ApiSystem.usuario.exception;

public class PersonalNaoTemAcessoException extends RuntimeException {
    public PersonalNaoTemAcessoException() {
        super("Apenas personais podem acessar este recurso.");
    }
}
