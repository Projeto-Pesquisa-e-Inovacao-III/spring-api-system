package com.spring.ApiSystem.agendamento.exception;

public class AgendamentoComAtencedenciaException extends RuntimeException {
    public AgendamentoComAtencedenciaException() {
        super("O agendamento deve ser feito com pelo menos 24 horas de antecedência.");
    }
}
