package com.spring.ApiSystem.domain.produtoexibicao.dto.response;

import com.spring.ApiSystem.domain.produtoexibicao.enums.TipoProduto;

public record ResProdutoExibicaoLimitAndSizeDto(
        TipoProduto tipoProduto,
        Integer limit,
        Integer size
) {}
