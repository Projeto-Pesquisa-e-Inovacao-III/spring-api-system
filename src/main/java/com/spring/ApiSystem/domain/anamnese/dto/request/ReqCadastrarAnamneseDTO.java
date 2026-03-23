package com.spring.ApiSystem.domain.anamnese.dto.request;

import com.spring.ApiSystem.domain.anamnese.dto.CondicoesDTO;
import com.spring.ApiSystem.domain.anamnese.dto.AnamneseDTO;
import com.spring.ApiSystem.domain.anamnese.enums.NivelDeAtividadeEnum;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record ReqCadastrarAnamneseDTO(
    @NotNull Double altura,
    @NotNull Double peso,
    @NotBlank String objectivoPrincipal,
    @Nullable String rotina,
    @NotNull List<CondicoesDTO> condicoes,
    @NotNull NivelDeAtividadeEnum nivelDeAtividade,
    @Nullable String observacaoSaude
) implements AnamneseDTO {}