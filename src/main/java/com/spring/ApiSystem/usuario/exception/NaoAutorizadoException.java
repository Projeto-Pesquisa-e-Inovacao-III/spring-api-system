package com.spring.ApiSystem.usuario.exception;

public class NaoAutorizadoException extends RuntimeException {
    public NaoAutorizadoException() {
        super("Usuario não autorizado");
    }
}