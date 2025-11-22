package com.spring.ApiSystem.endereco.dto.response;

import com.spring.ApiSystem.cep.dto.response.ResBuscarAgendamentosPersonalPorIdCEPDto;

public record ResAgendementoDadosEnderecoPersonalDTO(

        String numero,
        String complemento,
        String tipo,
        ResBuscarAgendamentosPersonalPorIdCEPDto cep
) { }
