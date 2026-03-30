package com.spring.ApiSystem.domain.disponibilidade.exception;

import com.spring.ApiSystem.shared.enums.DiaSemana;

public class DisponibilidadeInactiveException extends RuntimeException {
    public DisponibilidadeInactiveException(DiaSemana diaSemana) {
        super("Não há disponiblidade para " + diaSemana.getValorBD());
    }
}