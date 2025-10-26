package com.spring.ApiSystem.dto.endereco.request;

import com.spring.ApiSystem.dto.cep.response.CEPDto;
import jakarta.validation.constraints.NotBlank;

public record EnderecoDTO(
        @NotBlank(message = "Número é obrigatório")
    String numero,

        String complemento,

        @NotBlank(message = "Unidade é obrigatório")
    String unidade,

        @NotBlank(message = "Tipo é obrigatório")
    String tipo,

        CEPDto cep
) {}
