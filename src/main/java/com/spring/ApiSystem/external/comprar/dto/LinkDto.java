package com.spring.ApiSystem.external.comprar.dto;

import jakarta.validation.constraints.NotBlank;

public record LinkDto(
        @NotBlank String rel,
        @NotBlank String href,
        @NotBlank String method)
{ }
