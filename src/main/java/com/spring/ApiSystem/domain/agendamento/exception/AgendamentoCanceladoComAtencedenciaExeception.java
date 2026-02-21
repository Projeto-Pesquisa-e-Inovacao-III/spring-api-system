package com.spring.ApiSystem.domain.agendamento.exception;

public class AgendamentoCanceladoComAtencedenciaExeception extends RuntimeException {
    public AgendamentoCanceladoComAtencedenciaExeception() {
        super("O Cancelamento do agendamento deve ser feito com pelo menos 24 horas de antecedência.");
    }
}
