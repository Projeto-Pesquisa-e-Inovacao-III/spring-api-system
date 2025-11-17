package com.spring.ApiSystem.telefone.dto.request;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record ReqCadastrarTelefoneDTO(
        @NotBlank(message = "DDD não pode estar vazio")
        @Pattern(regexp = "\\d{2}", message = "DDD deve conter 2 dígitos")
        String ddd,

        @NotBlank(message = "Número não pode estar vazio")
        @Pattern(regexp = "\\d{8,9}", message = "Número deve conter 8 ou 9 dígitos")
        String numero,

        @NotBlank(message = "País não pode estar vazio")
        String pais) {}
