package com.spring.ApiSystem.domain.produtocontratado.dto.response;

public record ResIncrementarSaldoDTO(
        boolean sucesso,
        String mensagem,
        Integer saldoAtual
) {}