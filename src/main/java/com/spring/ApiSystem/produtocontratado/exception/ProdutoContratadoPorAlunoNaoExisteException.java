package com.spring.ApiSystem.produtocontratado.exception;

public class ProdutoContratadoPorAlunoNaoExisteException extends RuntimeException {
    public ProdutoContratadoPorAlunoNaoExisteException(Long idAluno) {
        super("O aluno com o ID " + idAluno + " não possui produtos contratados.");
    }
}
