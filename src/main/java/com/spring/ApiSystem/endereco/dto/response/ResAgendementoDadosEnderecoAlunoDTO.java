package com.spring.ApiSystem.endereco.dto.response;

import com.spring.ApiSystem.cep.dto.response.ResBuscarAgendamentosAlunosPorIdCEPDto;
import com.spring.ApiSystem.cep.dto.response.ResBuscarAgendamentosPersonalPorIdCEPDto;

public record ResAgendementoDadosEnderecoAlunoDTO(

        String numero,
        String complemento,
        String tipo,
        ResBuscarAgendamentosAlunosPorIdCEPDto cep
) { }
