package com.spring.ApiSystem.exception;

public class AlunoNaoExisteExcpetion extends RuntimeException {
    public AlunoNaoExisteExcpetion() {
        super("Aluno com esse ID não existe " );
    }
}
