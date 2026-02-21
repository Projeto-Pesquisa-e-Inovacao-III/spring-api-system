package com.spring.ApiSystem.domain.endereco.dto.request;

import com.spring.ApiSystem.domain.cep.dto.response.CEPDto;
import jakarta.validation.constraints.NotBlank;

public record ReqCadastrarEnderecoDTO(
        @NotBlank(message = "Número é obrigatório")
         String numero,

        String complemento,

        String unidade,

        @NotBlank(message = "Tipo é obrigatório")
        String tipo,

        CEPDto cep
) {}
