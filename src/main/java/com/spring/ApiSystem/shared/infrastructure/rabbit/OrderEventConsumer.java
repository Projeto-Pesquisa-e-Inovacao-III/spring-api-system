package com.spring.ApiSystem.shared.infrastructure.rabbit;

import com.spring.ApiSystem.shared.infrastructure.rabbit.dto.OrderPaidMessage;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnProperty(prefix = "messaging.rabbitmq", name = "enabled", havingValue = "true")
public class OrderEventConsumer {
    @RabbitListener(queues = "api_system.orders.queue")
    public void consumeOrderPaidEvent(OrderPaidMessage message) {
        System.out.println("Recebido: " + message.orderId());
    }
}
