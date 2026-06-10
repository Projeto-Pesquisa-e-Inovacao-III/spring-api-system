package com.spring.ApiSystem.domain.resumoAgendamento.projection;

import com.spring.ApiSystem.domain.agendamento.Agendamento;
import com.spring.ApiSystem.domain.resumoAgendamento.ResumoAgendamento;

public interface ResAgendamentoWithResumeProjection {
    Agendamento getAgendamento();
    ResumoAgendamento getResumoAgendamento();
}
