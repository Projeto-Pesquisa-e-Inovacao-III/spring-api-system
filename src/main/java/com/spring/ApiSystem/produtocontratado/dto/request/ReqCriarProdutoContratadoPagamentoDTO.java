package com.spring.ApiSystem.produtocontratado.dto.request;

import org.jetbrains.annotations.NotNull;

import java.time.LocalDateTime;
import java.util.List;

public record ReqCriarProdutoContratadoPagamentoDTO(
        Long checkoutId,
        List<Long> chargeId,
        Long consumerId,
        Long itemId,
        LocalDateTime payedAt
) {
    @NotNull
    @Override
    public String toString() {
        return "ReqCriarProdutoContratadoPagamentoDTO{" +
                "checkoutId=" + checkoutId +
                ", chargeId=" + chargeId +
                ", consumerId=" + consumerId +
                ", itemId=" + itemId +
                ", payedAt=" + (payedAt != null ? payedAt : "null") +
                '}';
    }
}
