package com.spring.ApiSystem.shared.infrastructure.rabbit.dto;

import java.util.List;

public record OrderPaidEvent(
        OrderId orderId,
        CheckoutId checkoutId,
        String gatewayOrderId,
        String customerId,
        List<String> itensId,
        ChargeId chargeId
) {
    @Override
    public String toString() {
        return "OrderPaidEvent{" +
                "orderId=" + orderId +
                ", checkoutId=" + checkoutId +
                ", gatewayOrderId='" + gatewayOrderId + '\'' +
                ", customerId='" + customerId + '\'' +
                ", itensId=" + itensId +
                ", chargeId=" + chargeId +
                '}';
    }
}