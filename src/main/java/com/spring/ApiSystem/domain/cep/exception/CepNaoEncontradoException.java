package com.spring.ApiSystem.domain.cep.exception;

public class CepNaoEncontradoException extends RuntimeException {
    public CepNaoEncontradoException() {
        super("CEP inválido. Por favor, verifique o CEP informado.");
    }
}
