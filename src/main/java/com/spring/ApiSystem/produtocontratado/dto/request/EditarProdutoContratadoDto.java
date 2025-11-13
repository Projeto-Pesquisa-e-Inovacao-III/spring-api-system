package com.spring.ApiSystem.produtocontratado.dto.request;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record EditarProdutoContratadoDto(
        @NotNull
        Long id,
        @NotNull
        Boolean situacao,
        @NotNull
        @FutureOrPresent
        LocalDate dataExpiracao,
        @NotNull
        Integer saldoAula,
        @NotNull
        Long alunoId,
        @NotNull
        Long produtoExibicaoId
) {}
