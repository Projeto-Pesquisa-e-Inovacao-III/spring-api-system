package com.spring.ApiSystem.agendamento.exception;

public class AgendamentoNaoPodeSerAprovadoException extends RuntimeException {
    public AgendamentoNaoPodeSerAprovadoException() {
        super("Agendamento não pode ser aprovado neste estado.");
    }
}
