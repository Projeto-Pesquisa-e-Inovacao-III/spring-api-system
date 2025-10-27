package com.spring.ApiPag.dto.CheckoutDto;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.sql.Timestamp;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record CheckoutDto(
        String reference_id,
        Timestamp expiration_date,
        CustomerDto customerDTO,
        Boolean customer_modifiable,
        List<ItemDto> items,
        Integer additional_amount,
        Integer discount_amount,
        ShippingDto shipping,
        List<PaymentMethodDto> payment_methods,
        List<PaymentMethodsConfigsDto> payment_methods_config,
        String soft_descriptor,
        String redirect_url,
        List<String> notification_urls,
        List<String> payment_notification_urls,
        RecurrencePlanDto recurrence_plan,
        String return_url
) {}
