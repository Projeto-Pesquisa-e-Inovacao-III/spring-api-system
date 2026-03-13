package com.spring.ApiSystem.domain.anamnese.dto.response;

import java.util.List;

import com.spring.ApiSystem.domain.anamnese.Condicoes;
import com.spring.ApiSystem.domain.anamnese.dto.AnamneseDto;
import com.spring.ApiSystem.domain.anamnese.enums.NivelDeAtividadeEnum;

public record ResAtualizarAnamneseDto(
        Double altura,
        Double peso,
        String objectivoPrincipal,
        String rotina,
        List<Condicoes> condicoes,
        NivelDeAtividadeEnum nivelDeAtividade,
        String observacaoSaude) implements AnamneseDto {
}