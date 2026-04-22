package com.spring.ApiSystem.external.comprar.dto.response;

import jakarta.validation.constraints.NotBlank;

public record LinkDto(
        @NotBlank String rel,
        @NotBlank String href,
        @NotBlank String method)
{ }
