package com.spring.ApiSystem.aluno.exception;

public class AlunoNaoExisteExcpetion extends RuntimeException {
    public AlunoNaoExisteExcpetion() {
        super("Aluno com esse ID não existe " );
    }
}
