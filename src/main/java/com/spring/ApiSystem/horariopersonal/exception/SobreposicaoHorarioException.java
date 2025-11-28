package com.spring.ApiSystem.horariopersonal.exception;

public class SobreposicaoHorarioException extends RuntimeException {
    public SobreposicaoHorarioException() {
        super("O horário informado se sobrepõe a um horário já cadastrado.");
    }
}
