package com.spring.ApiSystem.domain.produtoexibicao.exception;

public class ProdutoExibicaoOutOfLimitsException extends RuntimeException {
    public ProdutoExibicaoOutOfLimitsException(Integer limit) {
        super("Limite de " + limit + " produtos ativos atingido. Desative um produto para criar um novo.");
    }
}
