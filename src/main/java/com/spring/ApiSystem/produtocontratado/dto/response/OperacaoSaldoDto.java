package com.spring.ApiSystem.produtocontratado.dto.response;

import jakarta.validation.constraints.NotNull;

public record OperacaoSaldoDto(
        Long id,
        Boolean situacao,
        Integer saldo
) {}
