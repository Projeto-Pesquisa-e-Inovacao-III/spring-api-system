package com.spring.ApiSystem.produtocontratado.exception;

public class ProdutoContratadoNaoExisteExcpetion extends RuntimeException {
    public ProdutoContratadoNaoExisteExcpetion() {
        super("Produto contrarado com esse ID não existe " );
    }
}
