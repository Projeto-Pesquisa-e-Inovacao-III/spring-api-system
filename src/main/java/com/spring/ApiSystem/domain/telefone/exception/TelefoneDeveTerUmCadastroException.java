package com.spring.ApiSystem.telefone.exception;

public class TelefoneDeveTerUmCadastroException extends RuntimeException {
    public TelefoneDeveTerUmCadastroException() {
        super("O usuário deve ter pelo menos um telefone cadastrado.");
    }
}
