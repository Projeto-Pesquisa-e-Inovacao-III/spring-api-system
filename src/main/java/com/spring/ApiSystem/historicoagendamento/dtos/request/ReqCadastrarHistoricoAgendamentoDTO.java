package com.spring.ApiSystem.historicoagendamento.dtos.request;

import com.spring.ApiSystem.agendamento.Agendamento;
import com.spring.ApiSystem.agendamento.enums.AgendamentoStatus;
import com.spring.ApiSystem.endereco.Endereco;
import com.spring.ApiSystem.produtoexibicao.enums.TipoAula;
import com.spring.ApiSystem.usuario.Usuario;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

public record ReqCadastrarHistoricoAgendamentoDTO(
        @NotNull(message = "A data e hora não pode ser nula")
        LocalDateTime dataHora,

        @NotNull(message = "O tipo de aula não pode ser nulo")
        TipoAula tipoAula,

        @NotNull(message = "O status não pode ser nulo")
        AgendamentoStatus status,

        @NotNull(message = "O agendamento não pode ser nulo")
        Agendamento agendamento,

        @NotNull(message = "O usuário não pode ser nulo")
        Usuario usuario,

        @NotNull(message = "O endereço não pode ser nulo")
        Endereco endereco,

        @NotNull(message = "A descrição não pode ser nula")
        @Size(max = 200, message = "A descrição deve ter no máximo 200 caracteres")
        String descricao
) {}
