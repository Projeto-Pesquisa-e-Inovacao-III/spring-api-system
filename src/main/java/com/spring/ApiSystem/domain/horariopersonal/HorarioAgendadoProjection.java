package com.spring.ApiSystem.horariopersonal;

import com.spring.ApiSystem.domain.agendamento.enums.AgendamentoStatus;
import com.spring.ApiSystem.produtoexibicao.enums.TipoAula;

import java.time.LocalDateTime;

public interface HorarioAgendadoProjection {
    LocalDateTime getDataInicio();
    LocalDateTime getDataFim();
    TipoAula getTipoAula();
    AgendamentoStatus getStatus();
}
