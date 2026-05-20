package com.spring.ApiSystem.domain.resumoAgendamento;

import com.spring.ApiSystem.domain.agendamento.Agendamento;
import com.spring.ApiSystem.domain.aluno.Aluno;
import com.spring.ApiSystem.domain.aluno.AlunoService;
import com.spring.ApiSystem.domain.personal.Personal;
import com.spring.ApiSystem.domain.resumoAgendamento.dto.req.ReqCadastrarResumoAgendamentoDTO;
import com.spring.ApiSystem.domain.resumoAgendamento.dto.res.ResCadastrarResumoAgendamentoDTO;
import com.spring.ApiSystem.domain.resumoAgendamento.dto.res.ResResumoAgendamentoAlunoDTO;
import com.spring.ApiSystem.domain.resumoAgendamento.enums.GrupoMuscular;
import com.spring.ApiSystem.domain.resumoAgendamento.mapper.ResumoAgendamentoMapper;
import com.spring.ApiSystem.domain.usuario.security.JpaUserDetailsService;
import com.spring.ApiSystem.shared.dto.PaginaCursor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ResumoAgendamentoService {
    private final ResumoAgendamentoRepository resumoAgendamentoRepository;
    private final ResumoAgendamentoMapper resumoAgendamentoMapper;
    private final JpaUserDetailsService jpaUserDetailsService;
    private final AlunoService alunoService;

    public ResumoAgendamentoService(ResumoAgendamentoRepository resumoAgendamentoRepository,
                                    ResumoAgendamentoMapper resumoAgendamentoMapper,
                                    JpaUserDetailsService jpaUserDetailsService,
                                    AlunoService alunoService) {
        this.resumoAgendamentoRepository = resumoAgendamentoRepository;
        this.resumoAgendamentoMapper = resumoAgendamentoMapper;
        this.jpaUserDetailsService = jpaUserDetailsService;
        this.alunoService = alunoService;
    }

    public ResCadastrarResumoAgendamentoDTO cadastrar(Aluno aluno, Personal personal, Agendamento agendamento,
                                                      String resumo, List<GrupoMuscular> grupoMuscular) {
        ResumoAgendamento resumoAgendamentoCadastrado = resumoAgendamentoRepository.save(
                new ResumoAgendamento(null, aluno, personal, agendamento, resumo, grupoMuscular)
        );

        return resumoAgendamentoMapper.toResCadastrarResumoDTO(resumoAgendamentoCadastrado);
    }

    public PaginaCursor<ResResumoAgendamentoAlunoDTO> consultarResumoAluno(Long alunoId, Long proximoId, int limit){
        alunoService.buscarPorId(alunoId);
        Long personalId = jpaUserDetailsService.getCurrentPersonal().getId();

        Pageable pageable = PageRequest.of(0, limit + 1);

        List<ResumoAgendamento> resultados = resumoAgendamentoRepository.findByAlunoIdAndPersonalId(
                alunoId, personalId, proximoId, pageable
        );

        boolean temProximo = resultados.size() > limit;
        List<ResumoAgendamento> pagina = temProximo ? resultados.subList(0, limit) : resultados;
        Long proximoCursor = temProximo ? pagina.getLast().getId() : null;

        return new PaginaCursor<>(
                resumoAgendamentoMapper.toResResumoAgendamentoAlunoDTO(pagina),
                proximoCursor
        );
    }

    public GrupoMuscular[] listarGruposMusculares(){
        return GrupoMuscular.values();
    }
}
