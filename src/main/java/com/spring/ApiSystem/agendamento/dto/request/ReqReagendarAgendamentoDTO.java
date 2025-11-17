package com.spring.ApiSystem.agendamento.dto.request;

import com.spring.ApiSystem.endereco.dto.request.ReqCadastrarEnderecoDTO;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record ReqReagendarAgendamentoDTO(
        @NotNull(message = "A nova data é obrigatória")
        @Future(message = "A data deve ser futura")
        LocalDateTime novaData,

        String novaDescricao,

        Long enderecoExistenteId,

        @Valid ReqCadastrarEnderecoDTO novoEndereco,

        @NotNull(message = "O tipo de usuário é obrigatório")
        String tipoUsuario
) {}
