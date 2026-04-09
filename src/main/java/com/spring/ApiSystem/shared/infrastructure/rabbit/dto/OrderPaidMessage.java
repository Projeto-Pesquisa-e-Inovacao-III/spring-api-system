package com.spring.ApiSystem.shared.infrastructure.rabbit.dto;

import java.time.OffsetDateTime;

public record OrderPaidMessage(
        String orderId,
        String checkoutId,
        String gatewayOrderId,
        String chargeId,
        OffsetDateTime paidAt,
        OffsetDateTime occurredOn
) {}