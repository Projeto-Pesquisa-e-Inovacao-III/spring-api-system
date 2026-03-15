package com.spring.ApiSystem.domain.anamnese.dto;
import java.util.List;

import com.spring.ApiSystem.domain.anamnese.enums.NivelDeAtividadeEnum;

public interface AnamneseDto {
    Double altura();
    Double peso();
    String objectivoPrincipal();
    String rotina();
    List<CondicoesDto> condicoes();
    NivelDeAtividadeEnum nivelDeAtividade();
    String observacaoSaude();
}