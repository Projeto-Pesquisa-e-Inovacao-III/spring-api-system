package com.spring.ApiSystem.domain.resumoagendamento;

import com.spring.ApiSystem.domain.agendamento.Agendamento;
import com.spring.ApiSystem.domain.aluno.Aluno;
import com.spring.ApiSystem.domain.aluno.AlunoService;
import com.spring.ApiSystem.domain.personal.Personal;
import com.spring.ApiSystem.domain.resumoagendamento.dto.res.ResCadastrarResumoAgendamentoDTO;
import com.spring.ApiSystem.domain.resumoagendamento.dto.res.ResResumoAgendamentoAlunoDTO;
import com.spring.ApiSystem.domain.resumoagendamento.enums.GrupoMuscular;
import com.spring.ApiSystem.domain.resumoagendamento.mapper.ResumoAgendamentoMapper;
import com.spring.ApiSystem.domain.usuario.security.JpaUserDetailsService;
import com.spring.ApiSystem.shared.dto.PaginaCursor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Testes do ResumoAgendamentoService")
class ResumoAgendamentoServiceTest {

    @Mock
    private ResumoAgendamentoRepository resumoAgendamentoRepository;

    @Mock
    private ResumoAgendamentoMapper resumoAgendamentoMapper;

    @Mock
    private JpaUserDetailsService jpaUserDetailsService;

    @Mock
    private AlunoService alunoService;

    @InjectMocks
    private ResumoAgendamentoService resumoAgendamentoService;

    @Test
    @DisplayName("Deve cadastrar resumo de agendamento")
    void deveCadastrarResumo() {
        Aluno aluno = new Aluno();
        Personal personal = new Personal();
        Agendamento agendamento = new Agendamento();
        List<GrupoMuscular> grupos = List.of(GrupoMuscular.PEITO);
        ResumoAgendamento salvo = new ResumoAgendamento(1L, aluno, personal, agendamento, "Resumo", grupos);
        ResCadastrarResumoAgendamentoDTO dto = new ResCadastrarResumoAgendamentoDTO(1L, 2L, "Resumo", grupos);

        when(resumoAgendamentoRepository.save(any(ResumoAgendamento.class))).thenReturn(salvo);
        when(resumoAgendamentoMapper.toResCadastrarResumoDTO(salvo)).thenReturn(dto);

        ResCadastrarResumoAgendamentoDTO resultado = resumoAgendamentoService.cadastrar(
                aluno,
                personal,
                agendamento,
                "Resumo",
                grupos
        );

        ArgumentCaptor<ResumoAgendamento> captor = ArgumentCaptor.forClass(ResumoAgendamento.class);
        verify(resumoAgendamentoRepository).save(captor.capture());
        ResumoAgendamento enviado = captor.getValue();

        assertEquals(aluno, enviado.getAluno());
        assertEquals(personal, enviado.getPersonal());
        assertEquals("Resumo", enviado.getResumo());
        assertEquals(grupos, enviado.getGrupoMuscular());
        assertEquals(dto, resultado);
        verify(resumoAgendamentoMapper).toResCadastrarResumoDTO(salvo);
    }

    @Test
    @DisplayName("Deve retornar pagina com proximo cursor quando houver mais registros")
    void deveRetornarPaginaComProximoCursor() {
        Long alunoId = 1L;
        Long proximoCursor = 20L;
        int limit = 2;
        Personal personal = new Personal();
        personal.setId(5L);

        List<ResumoAgendamento> resultados = new ArrayList<>();
        resultados.add(criarResumo(10L));
        resultados.add(criarResumo(9L));
        resultados.add(criarResumo(8L));

        when(jpaUserDetailsService.getCurrentPersonal()).thenReturn(personal);
        when(resumoAgendamentoRepository.findByAlunoIdAndPersonalId(
                eq(alunoId),
                eq(5L),
                eq(proximoCursor),
                any(Pageable.class)
        )).thenReturn(resultados);

        when(resumoAgendamentoMapper.toResResumoAgendamentoAlunoDTO(
                ArgumentMatchers.<List<ResumoAgendamento>>any()
        )).thenReturn(List.of(new ResResumoAgendamentoAlunoDTO(
                "Aluno",
                "Personal",
                LocalDateTime.now(),
                "Resumo",
                List.of(GrupoMuscular.PEITO)
        )));

        PaginaCursor<ResResumoAgendamentoAlunoDTO> pagina =
                resumoAgendamentoService.consultarResumoAluno(alunoId, proximoCursor, limit);

        assertNotNull(pagina);
        assertEquals(9L, pagina.proximoCursor());
        verify(alunoService).buscarPorId(alunoId);
        verify(resumoAgendamentoMapper).toResResumoAgendamentoAlunoDTO(
                argThat((List<ResumoAgendamento> list) -> list.size() == 2)
        );
    }

    @Test
    @DisplayName("Deve retornar pagina sem proximo cursor quando nao houver mais registros")
    void deveRetornarPaginaSemProximoCursor() {
        Long alunoId = 1L;
        Long proximoCursor = null;
        int limit = 2;
        Personal personal = new Personal();
        personal.setId(5L);

        List<ResumoAgendamento> resultados = List.of(criarResumo(10L), criarResumo(9L));

        when(jpaUserDetailsService.getCurrentPersonal()).thenReturn(personal);
        when(resumoAgendamentoRepository.findByAlunoIdAndPersonalId(
                eq(alunoId),
                eq(5L),
                eq(proximoCursor),
                any(Pageable.class)
        )).thenReturn(resultados);

        when(resumoAgendamentoMapper.toResResumoAgendamentoAlunoDTO(any(List.class)))
                .thenReturn(List.of(new ResResumoAgendamentoAlunoDTO(
                        "Aluno",
                        "Personal",
                        LocalDateTime.now(),
                        "Resumo",
                        List.of(GrupoMuscular.PEITO)
                )));

        PaginaCursor<ResResumoAgendamentoAlunoDTO> pagina =
                resumoAgendamentoService.consultarResumoAluno(alunoId, proximoCursor, limit);

        assertNotNull(pagina);
        assertNull(pagina.proximoCursor());
        verify(resumoAgendamentoMapper).toResResumoAgendamentoAlunoDTO(
                argThat((List<ResumoAgendamento> list) -> list.size() == 2)
        );
    }

    @Test
    @DisplayName("Deve listar todos os grupos musculares")
    void deveListarGruposMusculares() {
        GrupoMuscular[] grupos = resumoAgendamentoService.listarGruposMusculares();
        assertArrayEquals(GrupoMuscular.values(), grupos);
    }

    private ResumoAgendamento criarResumo(Long id) {
        ResumoAgendamento resumo = new ResumoAgendamento();
        resumo.setId(id);
        return resumo;
    }
}
