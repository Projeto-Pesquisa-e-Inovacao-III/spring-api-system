package com.spring.ApiSystem.domain.produtocontratado.dto.request;

import jakarta.validation.constraints.NotNull;

public record ReqCriarProdutoContratadoDto(
        @NotNull
        Long idProdutoExibicao
) {}
