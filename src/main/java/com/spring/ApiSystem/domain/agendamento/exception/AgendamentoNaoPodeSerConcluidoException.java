package com.spring.ApiSystem.domain.agendamento.exception;

public class AgendamentoNaoPodeSerConcluidoException extends RuntimeException {
    public AgendamentoNaoPodeSerConcluidoException() {
        super("Agendamento não pode ser concluído antes da data/hora");
    }
}
