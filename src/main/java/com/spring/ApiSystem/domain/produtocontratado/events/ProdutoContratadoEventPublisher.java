package com.spring.ApiSystem.domain.produtocontratado.events;

import com.spring.ApiSystem.domain.produtocontratado.ProdutoContratado;

import java.util.List;

public class ProdutoContratadoEventPublisher {

    private final List<ProdutoContrataListener> listeners;

    public ProdutoContratadoEventPublisher(List<ProdutoContrataListener> listeners) {
        this.listeners = listeners;
    }

    public void publishProdutoContratadoCreatedEvent(ProdutoContratado produtoContratado) {
        listeners.forEach(listener -> listener.onProdutoContratadoCreated(produtoContratado));
    }
}
