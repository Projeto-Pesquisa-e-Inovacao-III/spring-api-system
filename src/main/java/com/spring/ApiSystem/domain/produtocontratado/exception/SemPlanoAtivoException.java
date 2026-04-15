package com.spring.ApiSystem.domain.produtocontratado.exception;

public class SemPlanoAtivoException extends RuntimeException {
    public SemPlanoAtivoException() {
        super("Não há plano ativo no momento");
    }
}
