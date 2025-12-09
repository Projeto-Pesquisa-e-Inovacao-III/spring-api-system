package com.spring.ApiSystem.comprar.exception;

public class CompraDeProdutoExibicaoInexistente extends RuntimeException {
    public CompraDeProdutoExibicaoInexistente(Long id) {
        super("Não foi possível comprar, o produto com o id "+ id + "não existe");
    }
}
