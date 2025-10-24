package com.spring.ApiSystem.exception;

public class SenhaNaoCorrespondeAtual extends RuntimeException {
    public SenhaNaoCorrespondeAtual() {
        super("Senha informada não corresponde a atual");
    }
}
