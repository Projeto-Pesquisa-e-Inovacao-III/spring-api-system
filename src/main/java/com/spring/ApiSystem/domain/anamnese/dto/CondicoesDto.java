package com.spring.ApiSystem.domain.anamnese.dto;

import com.spring.ApiSystem.domain.anamnese.enums.TipoCondicoes;

public record CondicoesDto(
    String situacao,
    TipoCondicoes tipo
) {}
