package com.spring.ApiSystem.eventos.agendamentos;

import com.spring.ApiSystem.agendamento.Agendamento;
import com.spring.ApiSystem.usuario.Usuario;

public interface AgendamentoListener {
    void onAgendamentoCreated(Agendamento agendamento);

    void onReagendamentoCreated(Agendamento agendamento, Usuario usuario);

    void onCancelamentoAgendamento(Agendamento agendamento, Usuario usuario);

    void onConclusaoAgendamento(Agendamento agendamento);

    void onAprovacaoAgendamento(Agendamento agendamento, Usuario usuario);

    void onAusenciaRegistradaPersonal(Agendamento agendamento);

    void onAusenciaRegistradaAluno(Agendamento agendamento);

    void onAusenciaRegistradaAlunoJustificado(Agendamento agendamento);
}
