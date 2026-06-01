package com.spring.ApiSystem.domain.recomendacaotreino.exception;

public class RecomendacaoTreinoInexistenteException extends RuntimeException {
    public RecomendacaoTreinoInexistenteException(Long id) {
        super("A recomendacao de treino com id: " + id + " não existe.");
    }
}
