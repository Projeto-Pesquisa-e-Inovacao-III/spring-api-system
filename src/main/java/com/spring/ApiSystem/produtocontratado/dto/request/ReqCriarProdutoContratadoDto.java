package com.spring.ApiSystem.produtocontratado.dto.request;

import jakarta.validation.constraints.NotNull;

public record ReqCriarProdutoContratadoDto(
        @NotNull
        Long idProdutoExibicao
) {}
