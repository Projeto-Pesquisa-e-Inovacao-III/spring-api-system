package com.spring.ApiSystem.domain.agendamento.exception;

public class AgendamentoNaoPodeSerAprovadoException extends RuntimeException {
    public AgendamentoNaoPodeSerAprovadoException() {
        super("Agendamento não pode ser aprovado neste estado.");
    }
}
