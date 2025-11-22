package com.spring.ApiSystem.usuario.exception;

public class AlunoNaoTemAcessoException extends RuntimeException {
    public AlunoNaoTemAcessoException() {
        super("Apenas personais podem acessar este recurso.");
    }
}
