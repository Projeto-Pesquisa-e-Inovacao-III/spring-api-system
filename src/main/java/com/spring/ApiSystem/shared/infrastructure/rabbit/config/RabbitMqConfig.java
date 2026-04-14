package com.spring.ApiSystem.shared.infrastructure.rabbit.config;

import org.aopalliance.intercept.MethodInterceptor;
import org.jspecify.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.AmqpConnectException;
import org.springframework.amqp.AmqpRejectAndDontRequeueException;
import org.springframework.amqp.rabbit.config.RetryInterceptorBuilder;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.Connection;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionListener;
import org.springframework.amqp.rabbit.retry.RejectAndDontRequeueRecoverer;
import org.springframework.amqp.support.converter.JacksonJsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.boot.amqp.autoconfigure.RabbitProperties;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;

import java.util.concurrent.atomic.AtomicBoolean;

@Configuration
public class RabbitMqConfig {

    private static final Logger log = LoggerFactory.getLogger(RabbitMqConfig.class);

    private final CachingConnectionFactory cachingConnectionFactory;
    private final RabbitProperties rabbitProperties;
    private final AtomicBoolean connectErrorReported = new AtomicBoolean(false);

    public RabbitMqConfig(CachingConnectionFactory cachingConnectionFactory,
                          RabbitProperties rabbitProperties) {
        this.cachingConnectionFactory = cachingConnectionFactory;
        this.rabbitProperties = rabbitProperties;
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
            log.error("ERRO RABBITMQ: Falha ao processar mensagem. Tentando novamente...", t);
            throw new AmqpRejectAndDontRequeueException("Falha ao processar mensagem", t);
        });

        return factory;
    }


    @EventListener(ApplicationReadyEvent.class)
    public void onApplicationReadyCheckRabbit() {
        int maxAttempts = 3;
        int intervalMs = 5000;

        for (int i = 1; i <= maxAttempts; i++) {
            try (Connection conn = cachingConnectionFactory.createConnection()) {
                if (conn != null) {
                    log.info("Conectado ao RabbitMQ com sucesso na tentativa {}/{}", i, maxAttempts);
                    return;
                }
            } catch (Exception ex) {
                if (i < maxAttempts) {
                    log.warn("Tentativa {}/{} falhou. Tentando novamente em {}s...", i, maxAttempts, intervalMs / 1000);
                    try {
                        Thread.sleep(intervalMs);
                    } catch (InterruptedException ie) {
                        Thread.currentThread().interrupt();
                        break;
                    }
                } else {
                    log.error("ERRO CRITICO: Não foi possível conectar ao RabbitMQ após {} tentativas.", maxAttempts);
                }
            }
        }
    }
}
