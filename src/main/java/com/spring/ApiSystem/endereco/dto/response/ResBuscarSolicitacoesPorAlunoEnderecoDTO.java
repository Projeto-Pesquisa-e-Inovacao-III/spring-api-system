package com.spring.ApiSystem.endereco.dto.response;

import com.spring.ApiSystem.cep.dto.response.ResBuscarSolicitacoesPorAlunoCEPDto;
import com.spring.ApiSystem.cep.dto.response.ResBuscarSolicitacoesPorPersonalCEPDto;

public record ResBuscarSolicitacoesPorAlunoEnderecoDTO(
        ResBuscarSolicitacoesPorAlunoCEPDto cep,
        String numero
){}
