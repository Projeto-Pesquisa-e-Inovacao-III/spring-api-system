package com.spring.ApiSystem.eventos.produtocontratado;

import com.spring.ApiSystem.domain.produtocontratado.ProdutoContratado;

public interface ProdutoContrataListener {
    void onProdutoContratadoCreated(ProdutoContratado produtoContratado);
}
