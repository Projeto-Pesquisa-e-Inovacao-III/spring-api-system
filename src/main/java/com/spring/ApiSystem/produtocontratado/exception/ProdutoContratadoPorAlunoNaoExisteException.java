package com.spring.ApiSystem.produtocontratado.exception;

public class ProdutoContratadoPorAlunoNaoExisteException extends RuntimeException {
    public ProdutoContratadoPorAlunoNaoExisteException() {
        super("Este aluno não possui produtos contratados.");
    }
}
