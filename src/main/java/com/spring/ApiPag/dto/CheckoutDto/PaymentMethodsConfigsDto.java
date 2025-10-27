package com.spring.ApiPag.dto.CheckoutDto;

import com.spring.ApiPag.enums.paymentMethod.PaymentMethodEnum;
import com.spring.ApiPag.validator.AllowedPaymentMethod.AllowedPaymentMethod;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record PaymentMethodsConfigsDto(
        @NotNull
        @AllowedPaymentMethod
        PaymentMethodEnum type,

        @NotNull
        List<ConfigOptionDto> configs
) {
}
