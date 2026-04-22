package com.spring.ApiSystem.domain.anamnese.exception;

public class AnamneseJaExisteException extends RuntimeException {
    public AnamneseJaExisteException() {
        super("Aluno já possui uma anamnese cadastrada.");
    }
}
