package com.spring.ApiSystem.domain.recomendacaotreino.exception;

public class RecomendacaoTreinoInexistenteException extends RuntimeException {
    public RecomendacaoTreinoInexistenteException(Long id) {
        super("O agendamento de " + id + " não possui recomendacao de treino existente.");
    }
}
