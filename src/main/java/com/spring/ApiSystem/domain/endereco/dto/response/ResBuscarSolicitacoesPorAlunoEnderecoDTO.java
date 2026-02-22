package com.spring.ApiSystem.domain.endereco.dto.response;

import com.spring.ApiSystem.domain.cep.dto.response.ResBuscarSolicitacoesPorAlunoCEPDto;

public record ResBuscarSolicitacoesPorAlunoEnderecoDTO(
        ResBuscarSolicitacoesPorAlunoCEPDto cep,
        String numero,
        String complemento
){}
