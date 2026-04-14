package com.spring.ApiSystem.shared.infrastructure.rabbit;

import com.spring.ApiSystem.domain.produtocontratado.ProdutoContratado;
import com.spring.ApiSystem.domain.produtocontratado.ProdutoContratadoService;
import com.spring.ApiSystem.external.comprar.ComprarService;
import com.spring.ApiSystem.shared.infrastructure.rabbit.dto.OrderPaidMessage;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class OrderEventConsumer {

    private final ProdutoContratadoService produtoContratadoService;

    public OrderEventConsumer(ProdutoContratadoService produtoContratadoService) {
        this.produtoContratadoService = produtoContratadoService;
    }

    @RabbitListener(queues = "api_system.orders.queue")
    public void consumeOrderPaidEvent(OrderPaidMessage message) {
        System.out.println("Recebi a msg");
        produtoContratadoService.criarProdutoContratadoPeloIdAluno(
                Long.parseLong(message.itensId().getFirst()),
                Long.parseLong(message.customerId())
        );
    }

}
