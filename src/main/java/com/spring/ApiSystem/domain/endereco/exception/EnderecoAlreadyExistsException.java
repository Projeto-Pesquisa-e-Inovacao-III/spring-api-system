package com.spring.ApiSystem.domain.endereco.exception;

public class EnderecoAlreadyExistsException extends RuntimeException {
    public EnderecoAlreadyExistsException() {
        super("Endereço já cadastrado");
    }
}
