package com.spring.ApiSystem.dto.endereco.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record EnderecoDTO(
    @NotBlank(message = "Número é obrigatório")
    String numero,

    String complemento,

    @NotBlank(message = "Unidade é obrigatório")
    String unidade,

    @NotBlank(message = "Tipo é obrigatório")
    String tipo,

    @Size(min = 8, max = 8, message = "O CEP deve conter 8 caracteres")
    String cep
) {}
