package com.spring.ApiPag.dto;

import com.spring.ApiPag.dto.CheckoutDto.LinksDto;
import com.spring.ApiPag.entity.Checkout;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * DTO for {@link Checkout}
 */
public record ActivateInactivateCheckoutDto(
        @NotBlank String id,
        @NotBlank String status,
        @NotNull LinksDto links
    )
{ }