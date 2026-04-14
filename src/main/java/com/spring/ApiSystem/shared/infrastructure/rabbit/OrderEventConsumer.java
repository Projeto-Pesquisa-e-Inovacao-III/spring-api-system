package com.spring.ApiSystem.shared.infrastructure.rabbit;

import com.spring.ApiSystem.domain.produtocontratado.ProdutoContratado;
import com.spring.ApiSystem.domain.produtocontratado.ProdutoContratadoService;
import com.spring.ApiSystem.external.comprar.ComprarService;
import com.spring.ApiSystem.shared.infrastructure.rabbit.dto.OrderPaidMessage;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnProperty(prefix = "messaging.rabbitmq", name = "enabled", havingValue = "true")
public class OrderEventConsumer {

    private final ProdutoContratadoService produtoContratadoService;

    public OrderEventConsumer(ProdutoContratadoService produtoContratadoService) {
        this.produtoContratadoService = produtoContratadoService;
    }

    @RabbitListener(queues = "api_system.orders.queue")
    public void consumeOrderPaidEvent(OrderPaidMessage message) {
        produtoContratadoService.criarProdutoContratadoPeloIdAluno(
                Long.parseLong(message.customerId()),
                Long.parseLong((String) message.itensId().getFirst())
        );
    }
}
