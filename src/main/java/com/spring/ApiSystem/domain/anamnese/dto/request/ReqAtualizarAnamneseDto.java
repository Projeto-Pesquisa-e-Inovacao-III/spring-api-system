package com.spring.ApiSystem.domain.anamnese.dto.request;

import com.spring.ApiSystem.domain.anamnese.Condicoes;
import com.spring.ApiSystem.domain.anamnese.dto.AnamneseDto;
import com.spring.ApiSystem.domain.anamnese.enums.NivelDeAtividadeEnum;

import java.util.List;

public record ReqAtualizarAnamneseDto(
    Double altura,
    Double peso,
    String objectivoPrincipal,
    String rotina,
    List<Condicoes> condicoes,
    NivelDeAtividadeEnum nivelDeAtividade,
    String observacaoSaude
) implements AnamneseDto {}