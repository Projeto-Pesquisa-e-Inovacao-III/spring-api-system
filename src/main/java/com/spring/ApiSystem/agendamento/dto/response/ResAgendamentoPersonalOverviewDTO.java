package com.spring.ApiSystem.agendamento.dto.response;

import com.spring.ApiSystem.agendamento.enums.AgendamentoStatus;
import com.spring.ApiSystem.endereco.dto.response.ResEnderecoAgendamentoDTO;
import com.spring.ApiSystem.produtoexibicao.enums.TipoAula;

import java.time.LocalDateTime;

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
) {}
