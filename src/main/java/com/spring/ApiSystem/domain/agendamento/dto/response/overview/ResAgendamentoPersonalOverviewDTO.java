package com.spring.ApiSystem.domain.agendamento.dto.response.overview;

import java.time.LocalDateTime;
import com.spring.ApiSystem.domain.agendamento.enums.AgendamentoStatus;
import com.spring.ApiSystem.domain.endereco.dto.response.ResEnderecoAgendamentoDTO;
import com.spring.ApiSystem.domain.produtoexibicao.enums.TipoAula;


public record ResAgendamentoPersonalOverviewDTO(
        Long agendamentoId,
        AgendamentoStatus agendamentoStatus,
        LocalDateTime data,
        LocalDateTime datafim,
        String personalNome,
        String alunoNome,
        String caminhoFoto,
        TipoAula tipoAula,
        ResEnderecoAgendamentoDTO endereco
) implements AgendamentoOverviewResponse {}