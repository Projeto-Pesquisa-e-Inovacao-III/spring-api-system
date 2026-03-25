package com.spring.ApiSystem.external.comprar.dto.request;

import java.util.List;

public record CheckoutRequestDto(
        CustomerDto customer,
        List<ItemDto> items,
        Integer additionalAmount,
        Integer discountAmount
) {
    public record CustomerDto(
            String externalCustomerId,
            String name,
            String email,
            TaxDocumentDto taxDocument,
            PhoneDto phone
    ) {}

    public record TaxDocumentDto(
            String value,
            String type
    ) {}

    public record PhoneDto(
            String country,
            String area,
            String number
    ) {}

    public record ItemDto(
            String externalItemId,
            String name,
            String description,
            Integer quantity,
            Integer unitAmount
    ) {}
}

