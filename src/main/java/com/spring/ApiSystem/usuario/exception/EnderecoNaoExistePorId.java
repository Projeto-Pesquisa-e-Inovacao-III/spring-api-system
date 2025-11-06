package com.spring.ApiSystem.usuario.exception;

public class EnderecoNaoExistePorId extends RuntimeException {
    public EnderecoNaoExistePorId() {
        super("Endereço não existente para o CEP informado");
    }
}
