package com.spring.ApiSystem.domain.produtocontratado.dto.response;

public record ResOperacaoSaldoDto(
        Long id,
        Boolean situacao,
        Integer saldo
) {}
