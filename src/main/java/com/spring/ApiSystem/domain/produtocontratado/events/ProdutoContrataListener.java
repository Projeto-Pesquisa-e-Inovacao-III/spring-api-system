package com.spring.ApiSystem.domain.produtocontratado.events;

import com.spring.ApiSystem.domain.produtocontratado.ProdutoContratado;

public interface ProdutoContrataListener {
    void onProdutoContratadoCreated(ProdutoContratado produtoContratado);
}
