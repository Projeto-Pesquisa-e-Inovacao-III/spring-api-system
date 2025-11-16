package com.spring.ApiSystem.horariopersonal.exception;

public class DiaSemanaNaoExisteExcption extends RuntimeException {
    public DiaSemanaNaoExisteExcption() {
        super("Dia da semana nao existe");
    }
}
