package com.spring.ApiSystem.domain.anamnese.dto.request;

import com.spring.ApiSystem.domain.anamnese.Condicoes;
import com.spring.ApiSystem.domain.anamnese.dto.AnamneseDto;
import com.spring.ApiSystem.domain.anamnese.enums.NivelDeAtividadeEnum;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record ReqCadastrarAnamneseDto(
    @NotNull Double altura,
    @NotNull Double peso,
    @NotBlank String objectivoPrincipal,
    @Nullable String rotina,
    @NotEmpty List<Condicoes> condicoes,
    @NotNull NivelDeAtividadeEnum nivelDeAtividade,
    @Nullable String observacaoSaude
) implements AnamneseDto {}