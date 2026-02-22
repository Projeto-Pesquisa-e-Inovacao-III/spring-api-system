package com.spring.ApiSystem.domain.horariopersonal.exception;

public class DiaSemanaNaoExisteExcption extends RuntimeException {
    public DiaSemanaNaoExisteExcption() {
        super("Dia da semana nao existe");
    }
}
