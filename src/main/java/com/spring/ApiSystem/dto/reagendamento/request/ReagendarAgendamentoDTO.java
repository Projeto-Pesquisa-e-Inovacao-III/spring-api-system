package com.spring.ApiSystem.dto.reagendamento.request;

import com.spring.ApiSystem.dto.endereco.request.EnderecoDTO;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record ReagendarAgendamentoDTO(
        @NotNull(message = "A nova data é obrigatória")
        @Future(message = "A data deve ser futura")
        LocalDateTime novaData,

        String novaDescricao,

        Long enderecoExistenteId,

        @Valid EnderecoDTO novoEndereco,

        @NotNull(message = "O tipo de usuário é obrigatório")
        String tipoUsuario
) {}
