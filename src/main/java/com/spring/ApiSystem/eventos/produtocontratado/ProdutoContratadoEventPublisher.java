package com.spring.ApiSystem.eventos.produtocontratado;

import java.util.ArrayList;
import java.util.List;

public class ProdutoContratadoEventPublisher {

    private final List<ProdutoContrataListener> listeners;

    public ProdutoContratadoEventPublisher(List<ProdutoContrataListener> listeners) {
        this.listeners = listeners;
    }

    public void publishProdutoContratadoCreatedEvent(com.spring.ApiSystem.produtocontratado.ProdutoContratado produtoContratado) {
        listeners.forEach(listener -> listener.onProdutoContratadoCreated(produtoContratado));
    }
}
