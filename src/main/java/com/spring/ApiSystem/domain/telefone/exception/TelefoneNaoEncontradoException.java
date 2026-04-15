package com.spring.ApiSystem.domain.telefone.exception;

public class TelefoneNaoEncontradoException extends RuntimeException {
    public TelefoneNaoEncontradoException() {
        super("Telefone não encontrado.");
    }
}
