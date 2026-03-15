package com.spring.ApiSystem.domain.anamnese.dto.request;

import com.spring.ApiSystem.domain.anamnese.dto.CondicoesDto;
import com.spring.ApiSystem.domain.anamnese.dto.AnamneseDto;
import com.spring.ApiSystem.domain.anamnese.enums.NivelDeAtividadeEnum;

import java.util.List;

public record ReqAtualizarAnamneseDto(
    Double altura,
    Double peso,
    String objectivoPrincipal,
    String rotina,
    List<CondicoesDto> condicoes,
    NivelDeAtividadeEnum nivelDeAtividade,
    String observacaoSaude
) implements AnamneseDto {}