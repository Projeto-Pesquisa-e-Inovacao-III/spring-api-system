package com.spring.ApiSystem.usuario.exception;

public class UsuarioNaoEncontradoException extends RuntimeException {
    public UsuarioNaoEncontradoException() {
        super("O usuário não foi encontrado");
    }
}
