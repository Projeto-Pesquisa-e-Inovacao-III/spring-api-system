package com.spring.ApiSystem.domain.agendamento.exception;

public class AgendamentoPersonalNaoPodeAlterarEnderecoException extends RuntimeException {
    public AgendamentoPersonalNaoPodeAlterarEnderecoException() {
        super("Personal não pode alterar o endereço.");    }
}
