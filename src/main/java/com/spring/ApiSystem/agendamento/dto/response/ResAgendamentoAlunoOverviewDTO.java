package com.spring.ApiSystem.agendamento.dto.response;

import com.spring.ApiSystem.agendamento.enums.AgendamentoStatus;
import com.spring.ApiSystem.endereco.dto.response.ResEnderecoAgendamentoDTO;
import com.spring.ApiSystem.produtoexibicao.enums.TipoAula;

import java.time.LocalDateTime;

public record ResAgendamentoAlunoOverviewDTO(
        Long agendamentoId,
        AgendamentoStatus agendamentoStatus,
        LocalDateTime data,
        LocalDateTime datafim,
        String personalNome,
        String alunoNome,
        TipoAula tipoAula,
        ResEnderecoAgendamentoDTO endereco
) {}