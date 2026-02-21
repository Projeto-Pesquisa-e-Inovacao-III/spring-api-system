package com.spring.ApiSystem.domain.aluno.exception;

public class AlunoNaoExisteException extends RuntimeException {
    public AlunoNaoExisteException() {
        super("Aluno com esse ID não existe");
    }
}
