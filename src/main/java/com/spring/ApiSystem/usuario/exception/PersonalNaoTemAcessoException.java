package com.spring.ApiSystem.usuario.exception;

public class PersonalNaoTemAcessoException extends RuntimeException {
    public PersonalNaoTemAcessoException() {
        super("Apenas alunos podem acessar este recurso.");
    }
}
