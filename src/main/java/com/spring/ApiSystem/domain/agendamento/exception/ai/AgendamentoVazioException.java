package com.spring.ApiSystem.domain.agendamento.exception.ai;

public class AgendamentoVazioException extends RuntimeException {
    public AgendamentoVazioException() {
        super("A solicitação de agendamento não pode estar vazia.");
    }
}
