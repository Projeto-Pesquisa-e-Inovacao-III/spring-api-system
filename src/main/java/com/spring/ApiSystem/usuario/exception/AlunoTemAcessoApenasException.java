package com.spring.ApiSystem.usuario.exception;

public class AlunoTemAcessoApenasException extends RuntimeException {
    public AlunoTemAcessoApenasException() {
        super("Apenas alunos podem acessar este recurso.");
    }
}
