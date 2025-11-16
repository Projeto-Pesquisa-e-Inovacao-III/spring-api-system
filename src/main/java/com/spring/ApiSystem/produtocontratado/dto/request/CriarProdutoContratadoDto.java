package com.spring.ApiSystem.produtocontratado.dto.request;

import jakarta.validation.constraints.NotNull;

public record CriarProdutoContratadoDto(
        @NotNull
        Long idProdutoExibicao
) {}
