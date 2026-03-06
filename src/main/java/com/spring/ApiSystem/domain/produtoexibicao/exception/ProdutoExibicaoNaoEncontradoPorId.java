package com.spring.ApiSystem.domain.produtoexibicao.exception;

public class    ProdutoExibicaoNaoEncontradoPorId extends RuntimeException {
    public ProdutoExibicaoNaoEncontradoPorId(Long id) {
        super("ProdutoExibicao não encontrado com ID: " + id);
    }
}
