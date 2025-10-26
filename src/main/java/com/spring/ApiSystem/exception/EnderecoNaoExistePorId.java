package com.spring.ApiSystem.exception;

public class EnderecoNaoExistePorId extends RuntimeException {
    public EnderecoNaoExistePorId() {
        super("Endereço não existente para o CEP informado");
    }
}
