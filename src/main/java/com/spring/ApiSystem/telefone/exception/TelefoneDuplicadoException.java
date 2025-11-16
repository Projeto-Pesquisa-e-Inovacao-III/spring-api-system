package com.spring.ApiSystem.telefone.exception;

public class TelefoneDuplicadoException extends RuntimeException {
    public TelefoneDuplicadoException() {
        super("Telefone já cadastrado no sistema");
    }
}
