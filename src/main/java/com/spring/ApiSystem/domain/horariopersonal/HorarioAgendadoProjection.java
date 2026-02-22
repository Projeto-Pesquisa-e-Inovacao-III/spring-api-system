package com.spring.ApiSystem.domain.horariopersonal;

import com.spring.ApiSystem.domain.agendamento.enums.AgendamentoStatus;
import com.spring.ApiSystem.domain.produtoexibicao.enums.TipoAula;


import java.time.LocalDateTime;

public interface HorarioAgendadoProjection {
    LocalDateTime getDataInicio();
    LocalDateTime getDataFim();
    TipoAula getTipoAula();
    AgendamentoStatus getStatus();
}
