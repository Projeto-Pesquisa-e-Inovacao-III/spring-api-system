package com.spring.ApiSystem.external.comprar.exception;

import com.spring.ApiSystem.domain.produtoexibicao.enums.TipoProduto;

public class AlunoJaPossuiProdutoContratadoDoTipoException extends RuntimeException {
    public AlunoJaPossuiProdutoContratadoDoTipoException(TipoProduto tipoProduto) {
        super("Não é possível prosseguir: o aluno já possui um produto do tipo " + tipoProduto + ".");
    }
}

