package com.spring.ApiSystem.service;

import com.spring.ApiSystem.model.Agendamento;
import com.spring.ApiSystem.repository.AgendamentoRepository;

public class AgendamentoService {

    private final AgendamentoRepository agendamentoRepository;

    public AgendamentoService(AgendamentoRepository agendamentoRepository) {
        this.agendamentoRepository = agendamentoRepository;
    }

    public Agendamento save(Agendamento agendamento) {
        return agendamentoRepository.save(agendamento);
    }
}
