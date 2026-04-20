package com.spring.ApiSystem.external.comprar.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record ReqCheckoutDto(
        CustomerDto customer,
        List<ItemDto> items,
        @JsonProperty("additional_amount") Integer additionalAmount,
        @JsonProperty("discount_amount") Integer discountAmount
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
            Long externalItemId,
            @JsonProperty("reference_id") String referenceId,
            String name,
            Integer quantity,
            Integer unitAmount
    ) {}
}

