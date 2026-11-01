package com.spring.ApiSystem.domain.resumoagendamento.projection;

import com.spring.ApiSystem.domain.agendamento.Agendamento;
import com.spring.ApiSystem.domain.resumoagendamento.ResumoAgendamento;

public interface ResAgendamentoWithResumeProjection {
    Agendamento getAgendamento();
    ResumoAgendamento getResumoAgendamento();
}
