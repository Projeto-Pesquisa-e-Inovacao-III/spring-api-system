package com.spring.ApiSystem.shared.infrastructure.rabbit.dto;

import java.time.OffsetDateTime;
import java.util.List;

public record OrderPaidMessage(
        String orderId,
        String checkoutId,
        String gatewayOrderId,
        String customerId,
        List<String> itensId,
        String chargeId,
        OffsetDateTime paidAt,
        OffsetDateTime occurredOn
) {}