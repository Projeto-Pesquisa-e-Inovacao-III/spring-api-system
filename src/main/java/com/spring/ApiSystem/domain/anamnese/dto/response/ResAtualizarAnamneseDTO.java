package com.spring.ApiSystem.domain.anamnese.dto.response;

import java.util.List;

import com.spring.ApiSystem.domain.anamnese.dto.CondicoesDTO;
import com.spring.ApiSystem.domain.anamnese.dto.AnamneseDTO;
import com.spring.ApiSystem.domain.anamnese.enums.NivelDeAtividadeEnum;

public record ResAtualizarAnamneseDTO(
        Double altura,
        Double peso,
        String objectivoPrincipal,
        String rotina,
        List<CondicoesDTO> condicoes,
        NivelDeAtividadeEnum nivelDeAtividade,
        String observacaoSaude) implements AnamneseDTO {
}