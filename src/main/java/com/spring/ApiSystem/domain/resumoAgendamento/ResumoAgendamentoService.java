package com.spring.ApiSystem.domain.resumoAgendamento;

import com.spring.ApiSystem.domain.resumoAgendamento.dto.req.ReqCadastrarResumoAgendamentoDTO;
import com.spring.ApiSystem.domain.resumoAgendamento.dto.res.ResCadastrarResumoAgendamentoDTO;
import com.spring.ApiSystem.domain.resumoAgendamento.enums.GrupoMuscular;
import com.spring.ApiSystem.domain.resumoAgendamento.mapper.ResumoAgendamentoMapper;
import org.springframework.stereotype.Service;

@Service
public class ResumoAgendamentoService {
    private final ResumoAgendamentoRepository resumoAgendamentoRepository;
    private final ResumoAgendamentoMapper resumoAgendamentoMapper;

    public ResumoAgendamentoService(ResumoAgendamentoRepository resumoAgendamentoRepository,
                                    ResumoAgendamentoMapper resumoAgendamentoMapper) {
        this.resumoAgendamentoRepository = resumoAgendamentoRepository;
        this.resumoAgendamentoMapper = resumoAgendamentoMapper;
    }

    public ResCadastrarResumoAgendamentoDTO cadastrar(ReqCadastrarResumoAgendamentoDTO dto) {
        ResumoAgendamento resumoAgendamentoCadastrado = resumoAgendamentoRepository.save(
                resumoAgendamentoMapper.toEntity(dto)
        );

        return resumoAgendamentoMapper.toResCadastrarResumoDTO(resumoAgendamentoCadastrado);
    }

    public GrupoMuscular[] listarGruposMusculares(){
        return GrupoMuscular.values();
    }
}
