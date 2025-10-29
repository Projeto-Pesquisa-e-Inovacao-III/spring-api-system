package com.spring.ApiSystem.agendamento.dto.request;

import com.spring.ApiSystem.endereco.dto.request.EnderecoDTO;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;
public record CriarAgendamentoDTO(
        @NotNull(message = "A data é obrigatória")
        LocalDateTime data,

        String descricao,

        Long enderecoExistenteId,

        @Valid EnderecoDTO novoEndereco,

        @NotNull(message = "O aluno é obrigatório")
        Long alunoId,

        @NotNull(message = "O personal é obrigatório")
        Long personalId,

        @NotNull(message = "O produto contratado é obrigatório")
        Long produtoContratadoId
) {
        public CriarAgendamentoDTO {
                boolean ambosNulos = enderecoExistenteId == null && novoEndereco == null;
                boolean ambosPreenchidos = enderecoExistenteId != null && novoEndereco != null;

                if (ambosNulos || ambosPreenchidos) {
                        throw new IllegalArgumentException(
                                "Informe APENAS enderecoExistenteId OU novoEndereco"
                        );
                }
        }
}
