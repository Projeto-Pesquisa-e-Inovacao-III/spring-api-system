package com.spring.ApiSystem.domain.agendamento.dto.response;



import com.spring.ApiSystem.domain.agendamento.enums.AgendamentoStatus;
import com.spring.ApiSystem.domain.endereco.dto.response.ResEnderecoAgendamentoDTO;
import com.spring.ApiSystem.domain.produtoexibicao.enums.TipoAula;

import java.time.LocalDateTime;

public record ResAgendamentoAlunoOverviewDTO(
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