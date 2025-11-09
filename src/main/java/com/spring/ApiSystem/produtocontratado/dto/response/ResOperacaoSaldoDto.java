package com.spring.ApiSystem.produtocontratado.dto.response;

public record ResOperacaoSaldoDto(
        Long id,
        Boolean situacao,
        Integer saldo
) {}
