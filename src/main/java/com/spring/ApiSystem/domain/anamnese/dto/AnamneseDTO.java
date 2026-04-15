package com.spring.ApiSystem.domain.anamnese.dto;
import java.util.List;

import com.spring.ApiSystem.domain.anamnese.enums.NivelDeAtividadeEnum;

public interface AnamneseDTO {
    Double altura();
    Double peso();
    String objectivoPrincipal();
    String rotina();
    List<CondicoesDTO> condicoes();
    NivelDeAtividadeEnum nivelDeAtividade();
    String observacaoSaude();
}