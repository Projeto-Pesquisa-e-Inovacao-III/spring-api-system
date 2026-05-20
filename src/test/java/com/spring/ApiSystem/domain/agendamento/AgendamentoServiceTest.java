package com.spring.ApiSystem.domain.agendamento;

import com.spring.ApiSystem.domain.agendamento.enums.AgendamentoStatus;
import com.spring.ApiSystem.domain.agendamento.events.AgendamentoEventPublisher;
import com.spring.ApiSystem.domain.agendamento.mapper.AgendamentoMapper;
import com.spring.ApiSystem.domain.agendamento.state.AgendamentoPendentePersonalConcluir;
import com.spring.ApiSystem.domain.aluno.Aluno;
import com.spring.ApiSystem.domain.aluno.AlunoService;
import com.spring.ApiSystem.domain.endereco.Endereco;
import com.spring.ApiSystem.domain.endereco.EnderecoService;
import com.spring.ApiSystem.domain.historicoagendamento.HistoricoAgendamentoService;
import com.spring.ApiSystem.domain.historicoagendamento.dtos.request.ReqCadastrarHistoricoAgendamentoDTO;
import com.spring.ApiSystem.domain.personal.Personal;
import com.spring.ApiSystem.domain.personal.PersonalService;
import com.spring.ApiSystem.domain.produtocontratado.ProdutoContratadoService;
import com.spring.ApiSystem.domain.produtoexibicao.enums.TipoAula;
import com.spring.ApiSystem.domain.resumoAgendamento.ResumoAgendamentoService;
import com.spring.ApiSystem.domain.resumoAgendamento.dto.req.ReqCadastrarResumoAgendamentoDTO;
import com.spring.ApiSystem.domain.resumoAgendamento.enums.GrupoMuscular;
import com.spring.ApiSystem.domain.usuario.Usuario;
import com.spring.ApiSystem.domain.usuario.enums.Role;
import com.spring.ApiSystem.domain.usuario.security.JpaUserDetailsService;
import com.spring.ApiSystem.shared.service.UtilsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Testes do AgendamentoService - Resumo Agendamento")
class AgendamentoServiceTest {

    @Mock
    private AgendamentoRepository agendamentoRepository;

    @Mock
    private PersonalService personalService;

    @Mock
    private ProdutoContratadoService produtoContratadoService;

    @Mock
    private AlunoService alunoService;

    @Mock
    private EnderecoService enderecoService;

    @Mock
    private ResumoAgendamentoService resumoAgendamentoService;

    @Mock
    private AgendamentoMapper agendamentoMapper;

    @Mock
    private HistoricoAgendamentoService historicoAgendamentoService;

    @Mock
    private JpaUserDetailsService jpaUserDetailsService;

    @Mock
    private AgendamentoEventPublisher agendamentoEventPublisher;

    @Mock
    private UtilsService utilsService;

    @InjectMocks
    private AgendamentoService agendamentoService;

    private Usuario usuarioPersonal;

    @BeforeEach
    void setUp() {
        usuarioPersonal = new Usuario();
        usuarioPersonal.setEmail("personal@email.com");
        usuarioPersonal.setRoles(Set.of(Role.PERSONAL));
    }

    @Test
    @DisplayName("Deve cadastrar resumo quando concluir agendamento pendente")
    void deveCadastrarResumoAoConfirmarConclusao() {
        Agendamento agendamento = criarAgendamentoPendente(LocalDateTime.now().minusHours(2));
        ReqCadastrarResumoAgendamentoDTO dto = new ReqCadastrarResumoAgendamentoDTO(
                "Resumo da aula",
                List.of(GrupoMuscular.PEITO)
        );
        ReqCadastrarHistoricoAgendamentoDTO historicoDto = criarHistoricoDto();

        when(jpaUserDetailsService.getCurrentUser()).thenReturn(usuarioPersonal);
        when(agendamentoRepository.buscarPorIdEEmailDoUsuario(1L, "personal@email.com"))
                .thenReturn(Optional.of(agendamento));
        when(agendamentoRepository.save(agendamento)).thenReturn(agendamento);
        when(agendamentoMapper.toReqCriarHistoricoAgendamentoDTO(agendamento)).thenReturn(historicoDto);

        agendamentoService.confirmarConclusao(1L, dto);

        verify(agendamentoRepository).save(agendamento);
        verify(historicoAgendamentoService).cadastrar(historicoDto, agendamento);
        verify(resumoAgendamentoService).cadastrar(
                agendamento.getAluno(),
                agendamento.getPersonal(),
                dto.resumo(),
                dto.grupoMuscular()
        );
        verify(agendamentoEventPublisher).publishAgendamentoConcluidoEvent(agendamento);
    }

    @Test
    @DisplayName("Nao deve cadastrar resumo quando status nao e pendente de conclusao")
    void naoDeveCadastrarResumoQuandoStatusNaoPendente() {
        Agendamento agendamento = criarAgendamentoAprovado(LocalDateTime.now().minusHours(2));
        ReqCadastrarResumoAgendamentoDTO dto = new ReqCadastrarResumoAgendamentoDTO(
                "Resumo da aula",
                List.of(GrupoMuscular.PEITO)
        );

        when(jpaUserDetailsService.getCurrentUser()).thenReturn(usuarioPersonal);
        when(agendamentoRepository.buscarPorIdEEmailDoUsuario(1L, "personal@email.com"))
                .thenReturn(Optional.of(agendamento));

        agendamentoService.confirmarConclusao(1L, dto);

        verify(resumoAgendamentoService, never()).cadastrar(any(), any(), any(), any());
        verify(agendamentoRepository, never()).save(any());
        verify(historicoAgendamentoService, never()).cadastrar(any(), any());
        verify(agendamentoEventPublisher).publishAgendamentoConcluidoEvent(agendamento);
    }

    private Agendamento criarAgendamentoPendente(LocalDateTime data) {
        Agendamento agendamento = criarAgendamentoBase(data);
        agendamento.setStatus(AgendamentoStatus.PENDENTE_PERSONAL_CONCLUIR);
        ReflectionTestUtils.setField(agendamento, "agendamentoState", new AgendamentoPendentePersonalConcluir());
        return agendamento;
    }

    private Agendamento criarAgendamentoAprovado(LocalDateTime data) {
        Agendamento agendamento = criarAgendamentoBase(data);
        agendamento.setStatus(AgendamentoStatus.APROVADO);
        return agendamento;
    }

    private Agendamento criarAgendamentoBase(LocalDateTime data) {
        Agendamento agendamento = new Agendamento();
        agendamento.setData(data);
        agendamento.setDataFim(data.plusMinutes(60));
        agendamento.setAluno(new Aluno());
        agendamento.setPersonal(new Personal());
        return agendamento;
    }

    private ReqCadastrarHistoricoAgendamentoDTO criarHistoricoDto() {
        return new ReqCadastrarHistoricoAgendamentoDTO(
                LocalDateTime.now(),
                LocalDateTime.now().plusMinutes(60),
                TipoAula.PRESENCIAL,
                AgendamentoStatus.CONCLUIDO,
                "Descricao",
                new Usuario(),
                new Endereco()
        );
    }
}

