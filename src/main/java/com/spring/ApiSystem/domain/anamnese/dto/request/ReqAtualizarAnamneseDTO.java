package com.spring.ApiSystem.domain.anamnese.dto.request;

import com.spring.ApiSystem.domain.anamnese.dto.CondicoesDTO;
import com.spring.ApiSystem.domain.anamnese.dto.AnamneseDTO;
import com.spring.ApiSystem.domain.anamnese.enums.NivelDeAtividadeEnum;

import java.util.List;

public record ReqAtualizarAnamneseDTO(
    Double altura,
    Double peso,
    String objectivoPrincipal,
    String rotina,
    List<CondicoesDTO> condicoes,
    NivelDeAtividadeEnum nivelDeAtividade,
    String observacaoSaude
) implements AnamneseDTO {}