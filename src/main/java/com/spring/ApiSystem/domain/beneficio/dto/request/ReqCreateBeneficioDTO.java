package com.spring.ApiSystem.domain.beneficio.dto.request;
import jakarta.validation.constraints.*;

public record ReqCreateBeneficioDTO(
        @NotBlank(message = "Valor do benefício não pode estar vazio")
        @Size(max = 50, message = "O beneficio deve conter no máximo 50 caracteres")
        String valor
        )
{}
