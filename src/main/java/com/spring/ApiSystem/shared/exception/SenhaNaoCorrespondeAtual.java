package com.spring.ApiSystem.shared.exception;

public class SenhaNaoCorrespondeAtual extends RuntimeException {
    public SenhaNaoCorrespondeAtual() {
        super("Senha informada não corresponde a atual");
    }
}
