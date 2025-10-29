package com.spring.ApiSystem.shared.exception;

public class UsuarioNaoEncontradoException extends RuntimeException {
    public UsuarioNaoEncontradoException() {
        super("O usuário não foi encontrado");
    }
}
