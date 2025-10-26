package com.spring.ApiSystem.exception;

public class UsuarioNaoEncontradoException extends RuntimeException {
    public UsuarioNaoEncontradoException() {
        super("O usuário não foi encontrado");
    }
}
