package com.spring.ApiPag.dto.CheckoutDto;

public record RecurrencePlanDto(
        String name,
        Integer billing_cyle,
        RecurrancePlanIntervalDto interval
) {
}
