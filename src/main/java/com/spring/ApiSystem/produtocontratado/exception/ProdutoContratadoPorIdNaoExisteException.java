package com.spring.ApiSystem.produtocontratado.exception;

public class ProdutoContratadoPorIdNaoExisteException extends RuntimeException {
    public ProdutoContratadoPorIdNaoExisteException(Long id) {
        super("Produto contratado com o ID " + id + " não existe");
    }
}
