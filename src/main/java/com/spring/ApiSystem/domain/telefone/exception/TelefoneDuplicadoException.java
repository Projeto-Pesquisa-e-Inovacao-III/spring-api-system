package com.spring.ApiSystem.domain.telefone.exception;

public class TelefoneDuplicadoException extends RuntimeException {
    public TelefoneDuplicadoException() {
        super("Telefone já cadastrado no sistema");
    }
}
