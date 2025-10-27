package com.spring.ApiPag.dto.CheckoutDto;

import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Range;

import java.io.Serializable;

/**
 * DTO for {@link com.spring.ApiPag.entity.DimesionsObject}
 */
public record DimesionsObjectDto(
        Long id, @NotNull @Range(min = 15, max = 100) Integer length,
        @NotNull @Range(min = 10, max = 100) Integer width,
        @NotNull @Range(min = 1, max = 100) Integer height) implements Serializable
{
}