package com.spring.ApiSystem.agendamento.exception;

public class AgendamentoExistenteNestaDataHorarioException extends RuntimeException {
    public AgendamentoExistenteNestaDataHorarioException() {
        super("Já existe um agendamento para este horário.");
    }
}
