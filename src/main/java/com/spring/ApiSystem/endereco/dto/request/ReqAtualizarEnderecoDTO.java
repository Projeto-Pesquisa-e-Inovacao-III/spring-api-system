package com.spring.ApiSystem.endereco.dto.request;

import com.spring.ApiSystem.cep.dto.response.CEPDto;
import jakarta.validation.constraints.NotBlank;

public record ReqAtualizarEnderecoDTO(
        @NotBlank(message = "Número é obrigatório")
        String numero,

        String complemento,

        String unidade,

        @NotBlank(message = "Tipo é obrigatório")
        String tipo,

        CEPDto cep
) {}
