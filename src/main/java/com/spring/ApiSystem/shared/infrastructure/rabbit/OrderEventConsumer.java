package com.spring.ApiSystem.shared.infrastructure.rabbit;

import com.spring.ApiSystem.domain.produtocontratado.ProdutoContratadoService;
import com.spring.ApiSystem.shared.infrastructure.rabbit.dto.OrderPaidEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.beans.factory.annotation.Value;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.OffsetDateTime;
import java.util.Base64;

@ConditionalOnProperty(name = "rabbitmq.enabled", havingValue = "true", matchIfMissing = true)
@Component
public class OrderEventConsumer {

    private static final Logger log = LoggerFactory.getLogger(OrderEventConsumer.class);
    private static final Duration TIMESTAMP_TOLERANCE = Duration.ofMinutes(5);
    private final ProdutoContratadoService produtoContratadoService;
    private final String hmacSecret;

    public OrderEventConsumer(ProdutoContratadoService produtoContratadoService,
                              @Value("${rabbitmq.hmac.secret}") String hmacSecret) {
        this.produtoContratadoService = produtoContratadoService;
        this.hmacSecret = hmacSecret;
    }

    @RabbitListener(queues = "api_system.orders.queue")
    public void consumeOrderPaidEvent(OrderPaidEvent message,
                                      @Header("x-idempotency-key") String idempotencyKey,
                                      @Header("x-timestamp") String timestampHeader) {
        if (!isTimestampValid(timestampHeader)) {
            log.warn("Timestamp invalido para o pedido do cliente: {}", message.customerId());
            return;
        }
        if (!isHmacValid(message, idempotencyKey)) {
            log.warn("Assinatura HMAC invalida para o pedido do cliente: {}", message.customerId());
            return;
        }

        log.info("Processando pedido do cliente: {}", message.customerId());
        produtoContratadoService.criarProdutoContratadoPeloIdAluno(
               Long.parseLong(message.itensId().getFirst().replaceAll("\\D", "")),
                Long.parseLong(message.customerId().replaceAll("\\D", ""))
        );
    }

    private boolean isTimestampValid(String timestampHeader) {
        if (timestampHeader == null || timestampHeader.isBlank()) {
            log.warn("Header x-timestamp vazio ou ausente");
            return false;
        }

        OffsetDateTime timestamp;
        timestamp = OffsetDateTime.parse(timestampHeader);

        OffsetDateTime now = OffsetDateTime.now();
        Duration diff = Duration.between(timestamp, now).abs();
        return diff.compareTo(TIMESTAMP_TOLERANCE) <= 0;
    }

    private boolean isHmacValid(OrderPaidEvent message, String signatureBase64) {
        if (hmacSecret == null || hmacSecret.isBlank()) {
            log.error("Segredo HMAC nao configurado (rabbitmq.hmac.secret)");
            return false;
        }
        if (signatureBase64 == null || signatureBase64.isBlank()) {
            log.warn("Header x-idempotency-key vazio ou ausente");
            return false;
        }
        if (message == null) {
            log.warn("Payload ausente para validacao HMAC");
            return false;
        }

        String payload = message.toString();
        System.out.println("payload: " + payload);
        String expectedBase64 = hmacSha256(payload, hmacSecret);

        return expectedBase64.equals(signatureBase64);
    }

    private String hmacSha256(String payload, String secret) {
        try {
            Mac mac = Mac.getInstance("HmacSHA256");
            mac.init(new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), "HmacSHA256"));
            byte[] digest = mac.doFinal(payload.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(digest);
        } catch (Exception ex) {
            throw new IllegalStateException("Erro ao gerar assinatura HMAC-SHA256", ex);
        }
    }
}
