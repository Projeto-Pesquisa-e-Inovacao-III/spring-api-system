package com.spring.ApiSystem.domain.disponibilidade.exception;

public class DiaSemanaNaoExisteException extends RuntimeException {
    public DiaSemanaNaoExisteException() {
        super("Dia da semana nao existe");
    }
}
