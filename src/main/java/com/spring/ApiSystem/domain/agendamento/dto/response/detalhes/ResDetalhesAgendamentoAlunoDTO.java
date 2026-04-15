package com.spring.ApiSystem.domain.agendamento.dto.response.detalhes;

import com.spring.ApiSystem.domain.agendamento.dto.response.detalhes.AgendamentoDetalheResponse;
import com.spring.ApiSystem.domain.agendamento.enums.AgendamentoStatus;
import com.spring.ApiSystem.domain.endereco.dto.response.ResAgendementoDadosEnderecoAlunoDTO;
import com.spring.ApiSystem.domain.produtoexibicao.enums.TipoAula;

import java.time.LocalDateTime;

public record ResDetalhesAgendamentoAlunoDTO(
        Long id,
        LocalDateTime dataInicio,
        LocalDateTime dataFim,
        int duracaoMinutos,
        AgendamentoStatus status,
        ResDetalhesAgendamentoPersonal personal,
        ResAgendementoDadosEnderecoAlunoDTO endereco,
        TipoAula tipoAula,
        String descricao
) implements AgendamentoDetalheResponse {

    public record ResDetalhesAgendamentoPersonal(
            Long id,
            String nome,
            String idade,
            String avatarUrl
    ) {}
}