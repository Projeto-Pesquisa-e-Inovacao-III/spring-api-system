package com.spring.ApiSystem.produtocontratado.dto.response;

public record ResIncrementarSaldoDTO(
        boolean sucesso,
        String mensagem,
        Integer saldoAtual
) {}