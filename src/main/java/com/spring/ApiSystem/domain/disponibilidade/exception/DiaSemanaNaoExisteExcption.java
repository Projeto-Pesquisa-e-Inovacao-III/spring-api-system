package com.spring.ApiSystem.domain.disponibilidade.exception;

public class DiaSemanaNaoExisteExcption extends RuntimeException {
    public DiaSemanaNaoExisteExcption() {
        super("Dia da semana nao existe");
    }
}
