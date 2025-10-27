package com.spring.ApiPag.dto.CheckoutDto;

import com.spring.ApiPag.enums.configOption.ConfigOptionEnum;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.NotBlank;

public record ConfigOptionDto(
        @NotNull(message = "Opção é obrigatória")
        ConfigOptionEnum option,

        @NotBlank(message = "Valor é obrigatório")
        String value
) { }

