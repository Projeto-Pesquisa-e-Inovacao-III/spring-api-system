package com.spring.ApiSystem.endereco.dto.request;

import com.spring.ApiSystem.cep.dto.response.CEPDto;
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
