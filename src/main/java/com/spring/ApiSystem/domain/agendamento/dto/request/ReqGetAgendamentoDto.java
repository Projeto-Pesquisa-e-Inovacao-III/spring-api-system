package com.spring.ApiSystem.domain.agendamento.dto.request;

import com.spring.ApiSystem.domain.agendamento.enums.AgendamentoStatus;
import com.spring.ApiSystem.domain.produtoexibicao.enums.TipoAula;

import java.time.LocalDateTime;

public record ReqGetAgendamentoDto(
        String nomeDoAluno,
        AgendamentoStatus status,
        TipoAula tipoAgendamento,
        LocalDateTime dataInic,
        LocalDateTime dataFim
) {}
