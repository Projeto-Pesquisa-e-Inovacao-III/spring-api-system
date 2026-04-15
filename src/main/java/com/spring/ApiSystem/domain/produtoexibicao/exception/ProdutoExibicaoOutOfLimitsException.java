package com.spring.ApiSystem.domain.produtoexibicao.exception;

import com.spring.ApiSystem.domain.produtoexibicao.enums.ProdutoExibicaoStatus;
import com.spring.ApiSystem.domain.produtoexibicao.enums.TipoProduto;

public class ProdutoExibicaoOutOfLimitsException extends RuntimeException {
    public ProdutoExibicaoOutOfLimitsException(Integer limit, TipoProduto tipoProduto,
                                               ProdutoExibicaoStatus status) {
        super("Limite de " + limit + " produtos do tipo " + tipoProduto + " com status "
                + status + " atingido. Desative um produto para criar um novo.");
    }
}
