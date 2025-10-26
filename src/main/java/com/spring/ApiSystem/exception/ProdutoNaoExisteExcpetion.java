package com.spring.ApiSystem.exception;

public class ProdutoNaoExisteExcpetion extends RuntimeException {
    public ProdutoNaoExisteExcpetion() {
        super("Produto contrarado com esse ID não existe " );
    }
}
