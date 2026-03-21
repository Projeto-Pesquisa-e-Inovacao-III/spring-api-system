package com.spring.ApiSystem.domain.anamnese.dto;

import com.spring.ApiSystem.domain.anamnese.enums.TipoCondicoes;

public record CondicoesDTO(
    String situacao,
    TipoCondicoes tipo
) {}
