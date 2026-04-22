package com.spring.ApiSystem.domain.historicoagendamento.dtos.request;

import com.spring.ApiSystem.domain.agendamento.enums.AgendamentoStatus;
import com.spring.ApiSystem.domain.endereco.Endereco;
import com.spring.ApiSystem.domain.produtoexibicao.enums.TipoAula;
import com.spring.ApiSystem.domain.usuario.Usuario;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

public record ReqCadastrarHistoricoAgendamentoDTO(
        @NotNull(message = "A data e hora não pode ser nula")
        LocalDateTime data,

        @NotNull(message = "A data fim e hora não pode ser nula")
        LocalDateTime dataFim,

        @NotNull(message = "O tipo de aula não pode ser nulo")
        TipoAula tipoAula,

        @NotNull(message = "O status não pode ser nulo")
        AgendamentoStatus status,

        @NotNull(message = "A descrição não pode ser nula")
        @Size(max = 200, message = "A descrição deve ter no máximo 200 caracteres")
        String descricao,

        @NotNull(message = "O usuario não pode ser nulo")
        Usuario usuario,

        @NotNull(message = "O endereço não pode ser nulo")
        Endereco endereco
) {}

