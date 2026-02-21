package com.spring.ApiSystem.domain.endereco.dto.response;

import com.spring.ApiSystem.domain.cep.dto.response.ResBuscarAgendamentosPersonalPorIdCEPDto;

public record ResAgendementoDadosEnderecoPersonalDTO(

        String numero,
        String complemento,
        String tipo,
        ResBuscarAgendamentosPersonalPorIdCEPDto cep
) { }
