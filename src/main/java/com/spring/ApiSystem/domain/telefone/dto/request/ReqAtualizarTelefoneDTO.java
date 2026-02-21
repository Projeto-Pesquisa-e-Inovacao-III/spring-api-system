package com.spring.ApiSystem.domain.telefone.dto.request;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record ReqAtualizarTelefoneDTO(
        @NotNull(message = "ID não pode ser nulo")
        Long id,

        @NotBlank(message = "DDD não pode estar vazio")
        @Pattern(regexp = "\\d{2}", message = "DDD deve conter 2 dígitos")
        String ddd,

        @NotBlank(message = "Número não pode estar vazio")
        @Pattern(regexp = "\\d{8,9}", message = "Número deve conter 8 ou 9 dígitos")
        String numero
) {}
