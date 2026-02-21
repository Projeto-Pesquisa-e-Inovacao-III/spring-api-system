package com.spring.ApiSystem.domain.agendamento.dto.request;

import com.spring.ApiSystem.domain.endereco.dto.request.ReqCadastrarEnderecoDTO;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record ReqReagendarAgendamentoDTO(
        @NotNull
        Long idAgendamento,
        @NotNull(message = "A nova data é obrigatória")
        @Future(message = "A data deve ser futura")
        LocalDateTime data,
        @Valid ReqCadastrarEnderecoDTO endereco,
        String descricao)
{}
