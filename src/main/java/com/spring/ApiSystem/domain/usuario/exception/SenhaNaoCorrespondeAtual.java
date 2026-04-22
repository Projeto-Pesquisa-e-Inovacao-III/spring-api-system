package com.spring.ApiSystem.domain.usuario.exception;

public class SenhaNaoCorrespondeAtual extends RuntimeException {
    public SenhaNaoCorrespondeAtual() {
        super("Senha informada não corresponde a atual");
    }
}
