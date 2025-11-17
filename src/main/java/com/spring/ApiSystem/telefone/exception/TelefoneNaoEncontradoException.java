package com.spring.ApiSystem.telefone.exception;

public class TelefoneNaoEncontradoException extends RuntimeException {
    public TelefoneNaoEncontradoException() {
        super("Telefone não encontrado.");
    }
}
