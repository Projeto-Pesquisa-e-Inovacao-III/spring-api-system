package com.spring.ApiSystem.agendamento.exception;

public class AgendamentoTipoDeAulaInvalido extends RuntimeException {
    public AgendamentoTipoDeAulaInvalido() {
        super("O tipo de aula informado é inválido: " );
    }
}
