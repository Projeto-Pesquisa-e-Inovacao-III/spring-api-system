package com.spring.ApiSystem.external.comprar.exception;

import com.spring.ApiSystem.domain.produtoexibicao.enums.TipoProduto;

public class AlunoAlreadyHaveProdutoContratadoByTipoProdutoException extends RuntimeException {
    public AlunoAlreadyHaveProdutoContratadoByTipoProdutoException(TipoProduto tipoProduto) {
        super("O Aluno já tem um produto contratado do tipo " + tipoProduto);
    }
}
