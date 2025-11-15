package com.spring.ApiSystem.produtocontratado.exception;

public class ProdutoContratadoAlunoNaoTemEsseProdutoException extends RuntimeException {
    public ProdutoContratadoAlunoNaoTemEsseProdutoException(Long id) {
        super("Este aluno não possui o produto contratado com o ID " + id);
    }
}
