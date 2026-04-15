package com.spring.ApiSystem.domain.usuario.exception;

public class NaoAutorizadoException extends RuntimeException {
    public NaoAutorizadoException() {
        super("Usuario não autorizado");
    }
}