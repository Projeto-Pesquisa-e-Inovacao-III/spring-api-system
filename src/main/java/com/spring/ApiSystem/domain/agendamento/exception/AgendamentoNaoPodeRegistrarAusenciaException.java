package com.spring.ApiSystem.domain.agendamento.exception;

public class AgendamentoNaoPodeRegistrarAusenciaException extends RuntimeException {
    public AgendamentoNaoPodeRegistrarAusenciaException() {
        super("Ausência só pode ser registrada após a data/hora do agendamento");
    }
}