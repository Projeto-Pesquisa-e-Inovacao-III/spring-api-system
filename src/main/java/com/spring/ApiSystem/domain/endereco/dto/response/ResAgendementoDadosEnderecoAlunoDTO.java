package com.spring.ApiSystem.endereco.dto.response;

import com.spring.ApiSystem.domain.cep.dto.response.ResBuscarAgendamentosAlunosPorIdCEPDto;

public record ResAgendementoDadosEnderecoAlunoDTO(

        String numero,
        String complemento,
        String tipo,
        ResBuscarAgendamentosAlunosPorIdCEPDto cep
) { }
