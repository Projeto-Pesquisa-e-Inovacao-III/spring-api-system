package com.spring.ApiSystem.domain.produtocontratado.exception;

public class ProdutoContratadoPorSituacaoNaoExisteException extends RuntimeException {
    public ProdutoContratadoPorSituacaoNaoExisteException(Boolean situacao) {
        super("Não há produtos contratados com a situação: " + situacao);
    }
}
