package com.spring.ApiSystem.domain.anamnese.dto.response;

import java.util.List;

import com.spring.ApiSystem.domain.anamnese.enums.NivelDeAtividadeEnum;

public record ResCadastrarAnamneseDto(
                Double altura,
                Double peso,
                String objectivoPrincipal,
                String rotina,
                List<String> condicoes,
                NivelDeAtividadeEnum nivelDeAtividade,
                String observacaoSaude
) {}