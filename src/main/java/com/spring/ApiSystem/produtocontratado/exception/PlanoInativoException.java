package com.spring.ApiSystem.produtocontratado.exception;

public class PlanoInativoException extends RuntimeException {
    public PlanoInativoException() {
        super("Não é possível mexer no saldo de um plano inativo.");
    }
}
