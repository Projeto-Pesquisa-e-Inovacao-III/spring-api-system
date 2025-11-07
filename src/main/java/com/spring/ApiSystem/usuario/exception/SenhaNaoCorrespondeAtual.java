package com.spring.ApiSystem.usuario.exception;

public class SenhaNaoCorrespondeAtual extends RuntimeException {
    public SenhaNaoCorrespondeAtual() {
        super("Senha informada não corresponde a atual");
    }
}
