package com.spring.ApiSystem.agendamento.exception;

public class AgendamentoNaoPertenceAoUsuarioException extends RuntimeException {
    public AgendamentoNaoPertenceAoUsuarioException() {
        super("Agendamento  não pertence a este úsuario.");
    }
}
