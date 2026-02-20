package com.spring.ApiSystem.historicoagendamento;

import com.spring.ApiSystem.domain.agendamento.Agendamento;
import com.spring.ApiSystem.historicoagendamento.dtos.request.ReqCadastrarHistoricoAgendamentoDTO;
import com.spring.ApiSystem.historicoagendamento.mapper.HistoricoMapper;
import org.springframework.stereotype.Service;

@Service
public class HistoricoAgendamentoService {
        private final HistoricoMapper historicoMapper;
        private final HistoricoAgendamentoRepository historicoAgendamentoRepository;

        public HistoricoAgendamentoService(HistoricoMapper historicoMapper, HistoricoAgendamentoRepository historicoAgendamentoRepository) {
            this.historicoMapper = historicoMapper;
            this.historicoAgendamentoRepository = historicoAgendamentoRepository;
        }

    public void cadastrar(ReqCadastrarHistoricoAgendamentoDTO dto, Agendamento agendamento) {
        var historicoAgendamento = historicoMapper.toEntity(dto, agendamento);
        historicoAgendamentoRepository.save(historicoAgendamento);
    }

}
