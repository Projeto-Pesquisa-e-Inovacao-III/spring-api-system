package com.spring.ApiSystem.endereco.exception;

public class EnderecoNaoExistePorId extends RuntimeException {
    public EnderecoNaoExistePorId() {
        super("Não há nenhum endereço cadastrado com o ID informado.");
    }
}
