package com.spring.ApiSystem.produtocontratado.exception;

public class ProdutoContratadoNaoEncontradoPorId extends RuntimeException {
    public ProdutoContratadoNaoEncontradoPorId(Long id) {
        super("Produto Contratado não encontrado com ID: " + id);
    }
}
