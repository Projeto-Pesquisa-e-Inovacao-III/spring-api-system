package com.spring.ApiSystem.domain.historicoagendamento;

import com.spring.ApiSystem.domain.agendamento.Agendamento;
import com.spring.ApiSystem.domain.agendamento.dto.response.ResTotalAgendamentoByStatusDto;
import com.spring.ApiSystem.domain.agendamento.projection.ResTotalAgendamentoByStatusProjection;
import com.spring.ApiSystem.domain.historicoagendamento.dtos.request.ReqCadastrarHistoricoAgendamentoDTO;
import com.spring.ApiSystem.domain.historicoagendamento.mapper.HistoricoMapper;
import com.spring.ApiSystem.domain.usuario.security.JpaUserDetailsService;
import org.springframework.stereotype.Service;

@Service
public class HistoricoAgendamentoService {
        private final HistoricoMapper historicoMapper;
        private final HistoricoAgendamentoRepository historicoAgendamentoRepository;
        private final JpaUserDetailsService jpaUserDetailsService;

    public HistoricoAgendamentoService(HistoricoMapper historicoMapper, HistoricoAgendamentoRepository historicoAgendamentoRepository, JpaUserDetailsService jpaUserDetailsService) {
        this.historicoMapper = historicoMapper;
        this.historicoAgendamentoRepository = historicoAgendamentoRepository;
        this.jpaUserDetailsService = jpaUserDetailsService;
    }

    public void cadastrar(ReqCadastrarHistoricoAgendamentoDTO dto, Agendamento agendamento) {
        var historicoAgendamento = historicoMapper.toEntity(dto, agendamento);
        historicoAgendamentoRepository.save(historicoAgendamento);
    }

    public ResTotalAgendamentoByStatusDto countTotalStatusAgendamentoByPersonal(){
        ResTotalAgendamentoByStatusProjection projection = historicoAgendamentoRepository.countTotalStatusAgendamentoByPersonal(
                jpaUserDetailsService.getCurrentUser().getId()
        );

        return new ResTotalAgendamentoByStatusDto(
                projection.getTotalPendente(),
                projection.getTotalRespondido(),
                projection.getTotalCanceladoPorMesAtual()
        );
    }

}
