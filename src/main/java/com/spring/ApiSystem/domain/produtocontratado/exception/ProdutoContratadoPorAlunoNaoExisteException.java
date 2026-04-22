package com.spring.ApiSystem.domain.produtocontratado.exception;

public class ProdutoContratadoPorAlunoNaoExisteException extends RuntimeException {
    public ProdutoContratadoPorAlunoNaoExisteException() {
        super("Este aluno não possui produtos contratados.");
    }
}
