package com.spring.ApiSystem.shared.infrastructure.rabbit;

import com.spring.ApiSystem.shared.infrastructure.rabbit.dto.OrderPaidMessage;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.config.RetryInterceptorBuilder;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.core.retry.RetryTemplate;
import org.springframework.stereotype.Component;
import org.yaml.snakeyaml.scanner.ScannerImpl;

@Component
public class OrderEventConsumer {
    @RabbitListener(queues = "api_system.orders.queue")
    public void consumeOrderPaidEvent(OrderPaidMessage message) {
        System.out.println("Recebido: " + message.orderId());
    }

}
