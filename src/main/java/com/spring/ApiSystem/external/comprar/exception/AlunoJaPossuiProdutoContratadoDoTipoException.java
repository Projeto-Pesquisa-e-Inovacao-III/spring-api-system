package com.spring.ApiSystem.external.comprar.exception;

import com.spring.ApiSystem.domain.produtoexibicao.enums.TipoProduto;

public class AlunoJaPossuiProdutoContratadoDoTipoException extends RuntimeException {
    public AlunoJaPossuiProdutoContratadoDoTipoException(TipoProduto tipoProduto) {
        super("Você já possui um pacote ativo. Para continuar agendando, adquira um pacote adicional.");
    }
}

