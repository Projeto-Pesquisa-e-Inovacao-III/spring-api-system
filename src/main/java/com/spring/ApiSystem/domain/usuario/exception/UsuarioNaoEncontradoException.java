package com.spring.ApiSystem.domain.usuario.exception;

public class UsuarioNaoEncontradoException extends RuntimeException {
    public UsuarioNaoEncontradoException() {
        super("O usuário não foi encontrado");
    }
}
