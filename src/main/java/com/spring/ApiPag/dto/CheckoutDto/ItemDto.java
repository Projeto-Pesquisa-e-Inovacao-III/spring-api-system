package com.spring.ApiPag.dto.CheckoutDto;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.URL;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record ItemDto(
        @NotNull
        @Length(min = 1, max = 100)
        String reference_id,
        @Length(min = 1, max = 100)
        String name,
        @Length(min = 1, max = 255)
        String description,
        @NotNull
        @Max(999)
        @Positive
        Integer quantity,
        @NotNull
        @Max(999999900)
        @Positive
        Integer unit_amount,
        @URL(protocol = "http") String image_url) {
}