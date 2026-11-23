package com.spring.ApiSystem.domain.resumoagendamento;

import com.spring.ApiSystem.domain.agendamento.Agendamento;
import com.spring.ApiSystem.domain.aluno.Aluno;
import com.spring.ApiSystem.domain.aluno.AlunoService;
import com.spring.ApiSystem.domain.musculosecundario.MusculoSecundario;
import com.spring.ApiSystem.domain.musculosecundario.MusculoSecundarioService;
import com.spring.ApiSystem.domain.personal.Personal;
import com.spring.ApiSystem.domain.resumoagendamento.dto.res.ResCadastrarResumoAgendamentoDTO;
import com.spring.ApiSystem.domain.resumoagendamento.dto.res.ResResumoAgendamentoAlunoDTO;
import com.spring.ApiSystem.domain.resumoagendamento.dto.res.ResResumoDTO;
import com.spring.ApiSystem.domain.resumoagendamento.mapper.ResumoAgendamentoMapper;
import com.spring.ApiSystem.domain.usuario.security.JpaUserDetailsService;
import com.spring.ApiSystem.shared.dto.PaginaCursor;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ResumoAgendamentoService {
    private final ResumoAgendamentoRepository resumoAgendamentoRepository;
    private final ResumoAgendamentoMapper resumoAgendamentoMapper;
    private final JpaUserDetailsService jpaUserDetailsService;
    private final AlunoService alunoService;
    private final MusculoSecundarioService musculoSecundarioService;

    public ResumoAgendamentoService(ResumoAgendamentoRepository resumoAgendamentoRepository,
                                    ResumoAgendamentoMapper resumoAgendamentoMapper,
                                    JpaUserDetailsService jpaUserDetailsService,
                                    AlunoService alunoService,
                                    MusculoSecundarioService musculoSecundarioService) {
        this.resumoAgendamentoRepository = resumoAgendamentoRepository;
        this.resumoAgendamentoMapper = resumoAgendamentoMapper;
        this.jpaUserDetailsService = jpaUserDetailsService;
        this.alunoService = alunoService;
        this.musculoSecundarioService = musculoSecundarioService;
    }

    @Transactional
    public ResCadastrarResumoAgendamentoDTO cadastrar(Aluno aluno, Personal personal, Agendamento agendamento,
                                                      String resumo, List<Long> grupoMuscular) {

        List<MusculoSecundario> listaMusculos = new ArrayList<>();

        grupoMuscular.forEach(
                (id) -> listaMusculos.add(musculoSecundarioService.getMusculoSecundarioPorId(id))
        );

        ResumoAgendamento resumoAgendamentoCadastrado = resumoAgendamentoRepository.save(
                new ResumoAgendamento(null, aluno, personal, agendamento, resumo, listaMusculos)
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

    public Page<ResResumoDTO> pegarResumoAlunoComAgendamento(Long alunoId, Pageable pageable) {
        Page<ResumoAgendamento> resumoAgendamentos = resumoAgendamentoRepository.getResumosWithAgendamentoByAlunoId(alunoId, pageable);
        return resumoAgendamentos.map(ResResumoDTO::from);
    }
}
