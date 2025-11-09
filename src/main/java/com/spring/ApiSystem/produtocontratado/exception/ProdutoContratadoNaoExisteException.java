package com.spring.ApiSystem.produtocontratado.exception;

public class ProdutoContratadoNaoExisteException extends RuntimeException {
    public ProdutoContratadoNaoExisteException() {
        super("Não existe nenhum produto contratado cadastrado no sistema.");
    }
}
