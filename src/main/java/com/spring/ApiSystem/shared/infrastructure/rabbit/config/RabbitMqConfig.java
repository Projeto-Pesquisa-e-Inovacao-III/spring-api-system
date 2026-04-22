package com.spring.ApiSystem.shared.infrastructure.rabbit.config;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.AmqpRejectAndDontRequeueException;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.Connection;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.support.converter.JacksonJsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;

@Configuration
public class RabbitMqConfig {

    private static final Logger log = LoggerFactory.getLogger(RabbitMqConfig.class);
    private final ConnectionFactory connectionFactory;

    public RabbitMqConfig(ConnectionFactory connectionFactory) {
        this.connectionFactory = connectionFactory;
    }

    @Bean
    public MessageConverter messageConverter() {
        return new JacksonJsonMessageConverter();
    }

    @Bean
    public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory(ConnectionFactory connectionFactory) {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setMessageConverter(messageConverter());
        factory.setMissingQueuesFatal(false);
        factory.setConcurrentConsumers(2);
        factory.setMaxConcurrentConsumers(5);

        factory.setErrorHandler(t -> {
            log.error("ERRO RABBITMQ: Falha no processamento.");
            throw new AmqpRejectAndDontRequeueException("Erro fatal", t);
        });

        return factory;
    }

    @EventListener(ApplicationReadyEvent.class)
    @Retry(name = "rabbitInit", fallbackMethod = "fallbackRabbit")
    @CircuitBreaker(name = "rabbitConnection", fallbackMethod = "fallbackRabbit")
    public void onApplicationReadyCheckRabbit() {
        log.info("RABBITMQ: Verificando integridade da conexão...");
        try (Connection conn = connectionFactory.createConnection()) {
            log.info("RABBITMQ: Conexão validada com sucesso!");
        }
    }

    public void fallbackRabbit(Exception e) {
        log.error("RESILIÊNCIA: Circuito aberto para RabbitMQ. O sistema operará em modo degradado (sem mensageria) até a recuperação automática.");
    }
}