package com.spring.ApiSystem.domain.recomendacaotreino.exception;

public class RecomendacaoTreinoExistenteException extends RuntimeException {
    public RecomendacaoTreinoExistenteException() {
        super("Este agendamento já possui uma recomendação de treino");
    }
}
