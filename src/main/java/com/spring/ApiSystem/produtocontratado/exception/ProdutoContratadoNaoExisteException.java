package com.spring.ApiSystem.produtocontratado.exception;

public class ProdutoContratadoNaoExisteException extends RuntimeException {
    public ProdutoContratadoNaoExisteException() {
        super("Produto contrarado com esse ID não existe " );
    }
}
