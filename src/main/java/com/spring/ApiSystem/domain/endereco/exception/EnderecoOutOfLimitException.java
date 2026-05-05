package com.spring.ApiSystem.domain.endereco.exception;

public class EnderecoOutOfLimitException extends RuntimeException {
    public EnderecoOutOfLimitException(Integer limitEndereco) {
        super("Limite de endereços atingido. O limite máximo é: " + limitEndereco);
    }
}
