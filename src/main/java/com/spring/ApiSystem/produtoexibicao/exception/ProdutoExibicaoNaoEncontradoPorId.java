package com.spring.ApiSystem.produtoexibicao.exception;

public class ProdutoExibicaoNaoEncontradoPorId extends RuntimeException {
    public ProdutoExibicaoNaoEncontradoPorId(Long id) {
        super("ProdutoExibicao não encontrado com ID: " + id);
    }
}
