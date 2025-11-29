package com.spring.ApiSystem.horariopersonal;

import com.spring.ApiSystem.agendamento.enums.AgendamentoStatus;
import com.spring.ApiSystem.produtoexibicao.enums.TipoAula;

import java.time.LocalDateTime;

public interface HorarioAgendadoProjection {
    LocalDateTime getDataInicio();
    TipoAula getTipoAula();
    AgendamentoStatus getStatus();
}
