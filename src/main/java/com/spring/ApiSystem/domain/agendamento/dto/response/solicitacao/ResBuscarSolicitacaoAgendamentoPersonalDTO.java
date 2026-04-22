package com.spring.ApiSystem.domain.agendamento.dto.response.solicitacao;

import com.spring.ApiSystem.domain.agendamento.dto.response.solicitacao.AgendamentoSolicitacaoResponse;
import com.spring.ApiSystem.domain.endereco.dto.response.ResBuscarSolicitacoesPorPersonalEnderecoDTO;
import com.spring.ApiSystem.domain.produtoexibicao.enums.TipoAula;
import com.spring.ApiSystem.domain.telefone.dto.response.ResBuscarSolicitacoesPorPersonalTelefoneDTO;

import java.time.LocalDateTime;

public record ResBuscarSolicitacaoAgendamentoPersonalDTO(
        Long agendamentoId,
        TipoAula tipoAula,
        String nome,
        ResBuscarSolicitacoesPorPersonalTelefoneDTO telefone,
        String idade,
        String foto,
        LocalDateTime dataInicio,
        LocalDateTime dataFim,
        ResBuscarSolicitacoesPorPersonalEnderecoDTO endereco,
        String status
) implements AgendamentoSolicitacaoResponse {}