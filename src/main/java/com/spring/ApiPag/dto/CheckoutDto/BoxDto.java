package com.spring.ApiPag.dto.CheckoutDto;

import jakarta.validation.constraints.NotNull;

/**
 * DTO for {@link com.spring.ApiPag.entity.Box}
 */
public record BoxDto(
        @NotNull String weight,
        @NotNull DimesionsObjectDto dimesionsObject)
{ }