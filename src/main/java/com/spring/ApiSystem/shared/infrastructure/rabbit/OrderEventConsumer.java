package com.spring.ApiSystem.shared.infrastructure.rabbit;

import com.spring.ApiSystem.domain.produtocontratado.ProdutoContratadoService;
import com.spring.ApiSystem.shared.infrastructure.rabbit.dto.OrderPaidMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;

@ConditionalOnProperty(name = "rabbitmq.enabled", havingValue = "true", matchIfMissing = true)
public class OrderEventConsumer {

    private static final Logger log = LoggerFactory.getLogger(OrderEventConsumer.class);
    private final ProdutoContratadoService produtoContratadoService;
    public OrderEventConsumer(ProdutoContratadoService produtoContratadoService) {
        this.produtoContratadoService = produtoContratadoService;
    }

    @RabbitListener(queues = "api_system.orders.queue")
    public void consumeOrderPaidEvent(OrderPaidMessage message) {
        log.info("Processando pedido do cliente: {}", message.customerId());
        produtoContratadoService.criarProdutoContratadoPeloIdAluno(
               Long.parseLong(message.itensId().getFirst().replaceAll("\\D", "")),
                Long.parseLong(message.customerId().replaceAll("\\D", ""))
        );
    }

}
