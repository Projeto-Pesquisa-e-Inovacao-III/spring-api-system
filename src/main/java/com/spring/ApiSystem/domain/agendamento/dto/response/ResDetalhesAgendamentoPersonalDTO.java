package com.spring.ApiSystem.agendamento.dto.response;

import java.time.LocalDateTime;

import com.spring.ApiSystem.endereco.dto.response.ResAgendementoDadosEnderecoAlunoDTO;
import com.spring.ApiSystem.produtoexibicao.enums.TipoAula;
import com.spring.ApiSystem.agendamento.enums.AgendamentoStatus;

public record ResDetalhesAgendamentoPersonalDTO(
        Long id,
        LocalDateTime dataInicio,
        LocalDateTime dataFim,
        int duracaoMinutos,
        AgendamentoStatus status,
        ResDetalhesAgendamentoAluno aluno,
        ResAgendementoDadosEnderecoAlunoDTO endereco,
        TipoAula tipoAula,
        String local,
        String descricao
) {
    public record ResDetalhesAgendamentoAluno(
            Long id,
            String nome,
            String idade,
            String avatarUrl
    ) {}
}
