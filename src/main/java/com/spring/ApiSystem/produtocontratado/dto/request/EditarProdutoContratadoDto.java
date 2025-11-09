package com.spring.ApiSystem.produtocontratado.dto.request;

import jakarta.validation.constraints.NotNull;

public record EditarProdutoContratadoDto(
        @NotNull
        Long id,
        @NotNull
        Boolean situacao,
        @NotNull
        Integer saldo
) {}
