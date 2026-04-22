package com.spring.ApiSystem.domain.disponibilidade.exception;

public class SobreposicaoHorarioException extends RuntimeException {
    public SobreposicaoHorarioException() {
        super("O horário informado se sobrepõe a um horário já cadastrado.");
    }
}
