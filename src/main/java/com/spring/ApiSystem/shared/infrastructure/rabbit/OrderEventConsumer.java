package com.spring.ApiSystem.shared.infrastructure.rabbit;

import com.spring.ApiSystem.shared.infrastructure.rabbit.dto.OrderPaidMessage;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class OrderEventConsumer {
    @RabbitListener(queues = "api_system.orders.queue")
    public void consumeOrderPaidEvent(OrderPaidMessage message) {
        System.out.println("Recebido: " + message.orderId());
    }

}
