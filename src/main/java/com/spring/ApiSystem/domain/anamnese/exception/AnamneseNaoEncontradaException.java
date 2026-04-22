package com.spring.ApiSystem.domain.anamnese.exception;

public class AnamneseNaoEncontradaException extends RuntimeException {
    public AnamneseNaoEncontradaException() {
        super("Nenhuma anamnese encontrada para este aluno.");
    }
}
