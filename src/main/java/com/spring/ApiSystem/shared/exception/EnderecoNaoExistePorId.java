package com.spring.ApiSystem.shared.exception;

public class EnderecoNaoExistePorId extends RuntimeException {
    public EnderecoNaoExistePorId() {
        super("Endereço não existente para o CEP informado");
    }
}
