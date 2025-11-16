package com.spring.ApiSystem.produtocontratado.exception;

public class SemPlanoAtivoException extends RuntimeException {
    public SemPlanoAtivoException() {
        super("Não há plano ativo no momento");
    }
}
