package com.spring.ApiSystem.domain.recomendacaotreino.exception;

public class PrecisaAgendamentoException extends RuntimeException {
    public PrecisaAgendamentoException() {
        super("Aluno precisa ao menos ter concluido um agendamento");
    }
}
