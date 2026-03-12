package com.spring.ApiSystem.domain.anamnese.dto.request;

import com.spring.ApiSystem.domain.anamnese.enums.NivelDeAtividadeEnum;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record ReqCadastrarAnamneseDto(
                @NotNull(message = "A altura é obrigatória")
                Double altura,

                @NotNull(message = "O peso é obrigatório")
                Double peso,

                @NotBlank(message = "O objetivo principal é obrigatório")
                String objectivoPrincipal,

                @Nullable 
                String rotina,

                //isso é obrigatório?
                @NotEmpty(message = "O objetivo principal é obrigatório")
                List<String> condicoes,

                @NotNull(message = "O objetivo principal é obrigatório")
                NivelDeAtividadeEnum nivelDeAtividade,

                @Nullable 
                String observacaoSaude

) {
}