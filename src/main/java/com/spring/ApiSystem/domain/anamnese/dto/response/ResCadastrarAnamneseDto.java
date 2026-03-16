package com.spring.ApiSystem.domain.anamnese.dto.response;

import java.util.List;

import com.spring.ApiSystem.domain.anamnese.dto.CondicoesDto;
import com.spring.ApiSystem.domain.anamnese.dto.AnamneseDto;
import com.spring.ApiSystem.domain.anamnese.enums.NivelDeAtividadeEnum;

public record ResCadastrarAnamneseDto(
                Double altura,
                Double peso,
                String objectivoPrincipal,
                String rotina,
                List<CondicoesDto> condicoes,
                NivelDeAtividadeEnum nivelDeAtividade,
                String observacaoSaude
) implements AnamneseDto {}