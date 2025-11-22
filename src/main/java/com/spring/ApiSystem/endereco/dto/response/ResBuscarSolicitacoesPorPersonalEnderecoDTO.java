package com.spring.ApiSystem.endereco.dto.response;

import com.spring.ApiSystem.cep.dto.response.ResBuscarSolicitacoesPorPersonalCEPDto;

public record ResBuscarSolicitacoesPorPersonalEnderecoDTO(
        ResBuscarSolicitacoesPorPersonalCEPDto cep,
        String numero
){}
