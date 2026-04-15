package com.spring.ApiSystem.domain.usuario.exception;

public class AlunoTemAcessoApenasException extends RuntimeException {
    public AlunoTemAcessoApenasException() {
        super("Apenas alunos podem acessar este recurso.");
    }
}
