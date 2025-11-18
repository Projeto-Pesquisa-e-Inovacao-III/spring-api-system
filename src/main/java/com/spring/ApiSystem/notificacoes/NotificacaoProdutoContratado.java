package com.spring.ApiSystem.notificacoes;

import com.spring.ApiSystem.eventos.produtocontratado.ProdutoContrataListener;
import com.spring.ApiSystem.produtocontratado.ProdutoContratado;
import org.springframework.stereotype.Component;

@Component
public class NotificacaoProdutoContratado implements ProdutoContrataListener {
    @Override
    public void onProdutoContratadoCreated(ProdutoContratado produtoContratado) {
        System.out.println("Notificação: Produto contratado com ID " + produtoContratado.getId() + " foi criado.");
    }
}
