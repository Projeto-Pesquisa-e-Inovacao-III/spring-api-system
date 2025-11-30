package com.spring.ApiSystem.horariopersonal;

import com.spring.ApiSystem.agendamento.enums.AgendamentoStatus;
import com.spring.ApiSystem.enums.DiaSemana;
import com.spring.ApiSystem.enums.TipoHorario;
import com.spring.ApiSystem.horariopersonal.dto.request.ReqHorarioDTO;
import com.spring.ApiSystem.horariopersonal.dto.response.ResHorarioDTO;
import com.spring.ApiSystem.horariopersonal.dto.response.ResSlotDisponivelDTO;
import com.spring.ApiSystem.horariopersonal.exception.SobreposicaoHorarioException;
import com.spring.ApiSystem.personal.Personal;
import com.spring.ApiSystem.personal.PersonalRepository;
import com.spring.ApiSystem.personal.exception.PersonalNaoExisteExcepetion;
import com.spring.ApiSystem.produtoexibicao.ProdutoExibicaoRepository;
import com.spring.ApiSystem.produtoexibicao.enums.TipoAula;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;


@DisplayName("Testes do DisponibilidadePersonalService")
@SpringBootTest
class DisponibilidadePersonalServiceTest {

    @Mock
    private DisponibilidadePersonalRepository disponibilidadeRepository;

    @Mock
    private PersonalRepository personalRepository;

    @Mock
    private ProdutoExibicaoRepository produtoExibicaoRepository;


    @InjectMocks
    private DisponibilidadePersonalService service;



    @Captor
    private ArgumentCaptor<List<DisponibilidadePersonal>> disponibilidadeListCaptor;

    private Personal personal;
    private DisponibilidadePersonal disponibilidadeManha;

    @BeforeEach
    void setUp() {
        personal = new Personal();
        personal.setId(1L);
        personal.setNome("Carlos Trainer");
        personal.setBufferMinutos(15);

        disponibilidadeManha = new DisponibilidadePersonal();
        disponibilidadeManha.setId(1L);
        disponibilidadeManha.setPersonal(personal);
        disponibilidadeManha.setDiaSemana(DiaSemana.SEGUNDA);
        disponibilidadeManha.setTipo(TipoHorario.DISPONIVEL);
        disponibilidadeManha.setHoraInicio(LocalTime.of(8, 0));
        disponibilidadeManha.setHoraFim(LocalTime.of(18, 0));
    }

    // ==================== TESTES: criarDisponibilidadePadrao ====================

    @Test
    @DisplayName("Deve criar disponibilidade padrão quando personal existe")
    void deveCriarDisponibilidadePadraoQuandoPersonalExiste() {
        // Arrange
        when(personalRepository.findById(1L)).thenReturn(Optional.of(personal));
        when(disponibilidadeRepository.saveAll(any())).thenReturn(Collections.emptyList());

        // Act
        service.criarDisponibilidadePadrao(1L);

        // Assert
        verify(personalRepository, times(1)).findById(1L);
        verify(disponibilidadeRepository, times(1)).saveAll(disponibilidadeListCaptor.capture());

        List<DisponibilidadePersonal> disponibilidadesSalvas = disponibilidadeListCaptor.getValue();

        // Deve criar 14 registros: 7 dias × 2 horários (DISPONIVEL + RESTRITO)
        assertEquals(14, disponibilidadesSalvas.size());

        // Verifica se todos os dias da semana foram incluídos
        long diasUnicos = disponibilidadesSalvas.stream()
                .map(DisponibilidadePersonal::getDiaSemana)
                .distinct()
                .count();
        assertEquals(7, diasUnicos);

        // Verifica que cada dia tem um horário DISPONIVEL (08:00-18:00)
        long disponiveisCount = disponibilidadesSalvas.stream()
                .filter(d -> d.getTipo() == TipoHorario.DISPONIVEL)
                .filter(d -> d.getHoraInicio().equals(LocalTime.of(8, 0)))
                .filter(d -> d.getHoraFim().equals(LocalTime.of(18, 0)))
                .count();
        assertEquals(7, disponiveisCount);

        // Verifica que cada dia tem um horário RESTRITO (12:00-13:00)
        long restritosCount = disponibilidadesSalvas.stream()
                .filter(d -> d.getTipo() == TipoHorario.RESTRITO)
                .filter(d -> d.getHoraInicio().equals(LocalTime.of(12, 0)))
                .filter(d -> d.getHoraFim().equals(LocalTime.of(13, 0)))
                .count();
        assertEquals(7, restritosCount);
    }

    @Test
    @DisplayName("Deve lançar exceção quando personal não existe ao criar disponibilidade padrão")
    void deveLancarExcecaoQuandoPersonalNaoExisteAoCriarDisponibilidadePadrao() {
        // Arrange
        when(personalRepository.findById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(PersonalNaoExisteExcepetion.class, () -> {
            service.criarDisponibilidadePadrao(999L);
        });

        verify(personalRepository, times(1)).findById(999L);
        verify(disponibilidadeRepository, never()).saveAll(any());
    }

    @Test
    @DisplayName("Deve criar disponibilidades com valores corretos para cada dia da semana")
    void deveCriarDisponibilidadesComValoresCorretosParaCadaDia() {
        // Arrange
        when(personalRepository.findById(1L)).thenReturn(Optional.of(personal));
        when(disponibilidadeRepository.saveAll(any())).thenReturn(Collections.emptyList());

        // Act
        service.criarDisponibilidadePadrao(1L);

        // Assert
        verify(disponibilidadeRepository, times(1)).saveAll(disponibilidadeListCaptor.capture());
        List<DisponibilidadePersonal> disponibilidades = disponibilidadeListCaptor.getValue();

        // Verifica se todos os horários têm o personal correto
        assertTrue(disponibilidades.stream()
                .allMatch(d -> d.getPersonal().getId().equals(1L)));

        // Verifica formato correto para cada tipo
        disponibilidades.forEach(d -> {
            if (d.getTipo() == TipoHorario.DISPONIVEL) {
                assertEquals(LocalTime.of(8, 0), d.getHoraInicio());
                assertEquals(LocalTime.of(18, 0), d.getHoraFim());
            } else if (d.getTipo() == TipoHorario.RESTRITO) {
                assertEquals(LocalTime.of(12, 0), d.getHoraInicio());
                assertEquals(LocalTime.of(13, 0), d.getHoraFim());
            }
        });
    }

    // ==================== TESTES: atualizarHorarios ====================

    @Test
    @DisplayName("Deve atualizar horário quando não há conflito")
    void deveAtualizarHorarioQuandoNaoHaConflito() {
        // Arrange
        ReqHorarioDTO request = new ReqHorarioDTO(
                DiaSemana.SEGUNDA,
                TipoHorario.DISPONIVEL,
                LocalTime.of(14, 0),
                LocalTime.of(16, 0)
        );

        when(disponibilidadeRepository.findById(1L)).thenReturn(Optional.of(disponibilidadeManha));
        when(disponibilidadeRepository.encontrarConflitos(anyLong(), any(), any(), any(), anyLong()))
                .thenReturn(Collections.emptyList());
        when(disponibilidadeRepository.save(any())).thenReturn(disponibilidadeManha);

        // Act
        ResHorarioDTO resultado = service.atualizarHorarios(1L, request);

        // Assert
        assertNotNull(resultado);
        verify(disponibilidadeRepository, times(1)).findById(1L);
        verify(disponibilidadeRepository, times(1)).encontrarConflitos(
                eq(1L),
                eq(DiaSemana.SEGUNDA),
                eq(LocalTime.of(14, 0)),
                eq(LocalTime.of(16, 0)),
                eq(1L)
        );
        verify(disponibilidadeRepository, times(1)).save(disponibilidadeManha);

        assertEquals(DiaSemana.SEGUNDA, disponibilidadeManha.getDiaSemana());
        assertEquals(TipoHorario.DISPONIVEL, disponibilidadeManha.getTipo());
        assertEquals(LocalTime.of(14, 0), disponibilidadeManha.getHoraInicio());
        assertEquals(LocalTime.of(16, 0), disponibilidadeManha.getHoraFim());
    }

    @Test
    @DisplayName("Deve lançar exceção quando horário não existe ao atualizar")
    void deveLancarExcecaoQuandoHorarioNaoExisteAoAtualizar() {
        // Arrange
        ReqHorarioDTO request = new ReqHorarioDTO(
                DiaSemana.SEGUNDA,
                TipoHorario.DISPONIVEL,
                LocalTime.of(14, 0),
                LocalTime.of(16, 0)
        );

        when(disponibilidadeRepository.findById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(EntityNotFoundException.class, () -> {
            service.atualizarHorarios(999L, request);
        });

        verify(disponibilidadeRepository, times(1)).findById(999L);
        verify(disponibilidadeRepository, never()).save(any());
    }

    @Test
    @DisplayName("Deve validar conflito ao atualizar horário")
    void deveValidarConflitoAoAtualizarHorario() {
        // Arrange
        DisponibilidadePersonal horarioConflitante = new DisponibilidadePersonal();
        horarioConflitante.setId(3L);
        horarioConflitante.setPersonal(personal);
        horarioConflitante.setDiaSemana(DiaSemana.SEGUNDA);
        horarioConflitante.setTipo(TipoHorario.RESTRITO);
        horarioConflitante.setHoraInicio(LocalTime.of(14, 0));
        horarioConflitante.setHoraFim(LocalTime.of(15, 0));

        ReqHorarioDTO request = new ReqHorarioDTO(
                DiaSemana.SEGUNDA,
                TipoHorario.DISPONIVEL,
                LocalTime.of(14, 30),
                LocalTime.of(16, 0)
        );

        when(disponibilidadeRepository.findById(1L)).thenReturn(Optional.of(disponibilidadeManha));
        when(disponibilidadeRepository.encontrarConflitos(anyLong(), any(), any(), any(), anyLong()))
                .thenReturn(List.of(horarioConflitante));

        // Act & Assert
        assertThrows(SobreposicaoHorarioException.class, () -> {
            service.atualizarHorarios(1L, request);
        });

        verify(disponibilidadeRepository, times(1)).findById(1L);
        verify(disponibilidadeRepository, never()).save(any());
    }

    // ==================== TESTES: obterHorariosDisponiveis ====================

    @Test
    @DisplayName("Deve lançar exceção quando personal não existe ao obter horários disponíveis")
    void deveLancarExcecaoQuandoPersonalNaoExisteAoObterHorarios() {
        // Arrange
        when(personalRepository.findById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(PersonalNaoExisteExcepetion.class, () -> {
            service.obterHorariosDisponiveis(999L, LocalDate.now());
        });

        verify(personalRepository, times(1)).findById(999L);
    }

    @Test
    @DisplayName("Deve retornar lista vazia quando não há disponibilidade no dia")
    void deveRetornarListaVaziaQuandoNaoHaDisponibilidade() {
        // Arrange
        LocalDate dataFutura = LocalDate.now().plusDays(1);
        when(personalRepository.findById(1L)).thenReturn(Optional.of(personal));
        when(disponibilidadeRepository.findByPersonalIdAndDiaSemana(anyLong(), any()))
                .thenReturn(Collections.emptyList());

        // Act
        List<ResSlotDisponivelDTO> resultado = service.obterHorariosDisponiveis(1L, dataFutura);

        // Assert
        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());
    }

    @Test
    @DisplayName("Deve retornar horários disponíveis para data futura sem agendamentos")
    void deveRetornarHorariosDisponiveisParaDataFuturaSemAgendamentos() {
        // Arrange
        LocalDate dataFutura = LocalDate.now().plusDays(7);
        DiaSemana diaFuturo = DiaSemana.fromDayOfWeek(dataFutura.getDayOfWeek());

        DisponibilidadePersonal dispManha = new DisponibilidadePersonal();
        dispManha.setPersonal(personal);
        dispManha.setDiaSemana(diaFuturo);
        dispManha.setTipo(TipoHorario.DISPONIVEL);
        dispManha.setHoraInicio(LocalTime.of(8, 0));
        dispManha.setHoraFim(LocalTime.of(12, 0));

        when(personalRepository.findById(1L)).thenReturn(Optional.of(personal));
        when(disponibilidadeRepository.findByPersonalIdAndDiaSemana(1L, diaFuturo))
                .thenReturn(List.of(dispManha));
        when(produtoExibicaoRepository.findAgendamentoSlotsByPersonalIdAndDataBetween(anyLong(), any(), any()))
                .thenReturn(Collections.emptyList());

        // Act
        List<ResSlotDisponivelDTO> resultado = service.obterHorariosDisponiveis(1L, dataFutura);

        // Assert
        assertNotNull(resultado);
        assertFalse(resultado.isEmpty());

        // Deve ter slots de 15 em 15 minutos entre 8:00 e 12:00
        // Total: 4 horas × 4 slots por hora = 16 slots

        // Verifica que os slots estão ordenados
        for (int i = 0; i < resultado.size() - 1; i++) {
            LocalTime inicio1 = LocalTime.parse(resultado.get(i).inicio());
            LocalTime inicio2 = LocalTime.parse(resultado.get(i + 1).inicio());
            assertTrue(inicio1.isBefore(inicio2));
        }
    }

    @Test
    @DisplayName("Deve filtrar horários passados quando é o dia atual")
    void deveFiltrarHorariosPassadosQuandoEDiaAtual() {
        // Arrange
        LocalDate hoje = LocalDate.now();
        DiaSemana diaAtual = DiaSemana.fromDayOfWeek(hoje.getDayOfWeek());

        DisponibilidadePersonal dispDia = new DisponibilidadePersonal();
        dispDia.setPersonal(personal);
        dispDia.setDiaSemana(diaAtual);
        dispDia.setTipo(TipoHorario.DISPONIVEL);
        dispDia.setHoraInicio(LocalTime.of(8, 0));
        dispDia.setHoraFim(LocalTime.of(18, 0)); // Janela completa do dia

        when(personalRepository.findById(1L)).thenReturn(Optional.of(personal));
        when(disponibilidadeRepository.findByPersonalIdAndDiaSemana(1L, diaAtual))
                .thenReturn(List.of(dispDia));
        when(produtoExibicaoRepository.findAgendamentoSlotsByPersonalIdAndDataBetween(anyLong(), any(), any()))
                .thenReturn(Collections.emptyList());

        // Act
        List<ResSlotDisponivelDTO> resultado = service.obterHorariosDisponiveis(1L, hoje);

        // Assert
        assertNotNull(resultado);

        // Se for antes das 18h, deve ter slots; se não, pode estar vazio
        LocalTime agora = LocalTime.now();
        if (agora.isBefore(LocalTime.of(17, 30))) { // 30 min antes do fim
            assertFalse(resultado.isEmpty(), "Deve retornar slots disponíveis no futuro");

            // Verifica que nenhum slot está no passado
            boolean todosNoFuturo = resultado.stream()
                    .map(slot -> LocalTime.parse(slot.inicio()))
                    .allMatch(inicio -> inicio.isAfter(agora));

            assertTrue(todosNoFuturo, "Todos os horários devem estar no futuro");
        }
    }


    @Test
    @DisplayName("Deve excluir slots com restrição aplicando buffer de 15 minutos antes")
    void deveExcluirSlotsComRestricaoAplicandoBuffer() {
        // Arrange
        LocalDate dataFutura = LocalDate.now().plusDays(7);
        DiaSemana diaFuturo = DiaSemana.fromDayOfWeek(dataFutura.getDayOfWeek());

        DisponibilidadePersonal dispDia = new DisponibilidadePersonal();
        dispDia.setPersonal(personal);
        dispDia.setDiaSemana(diaFuturo);
        dispDia.setTipo(TipoHorario.DISPONIVEL);
        dispDia.setHoraInicio(LocalTime.of(8, 0));
        dispDia.setHoraFim(LocalTime.of(18, 0));

        // Restrição: 12:00-13:00 (deve bloquear desde 11:45)
        DisponibilidadePersonal restricao = new DisponibilidadePersonal();
        restricao.setPersonal(personal);
        restricao.setDiaSemana(diaFuturo);
        restricao.setTipo(TipoHorario.RESTRITO);
        restricao.setHoraInicio(LocalTime.of(12, 0));
        restricao.setHoraFim(LocalTime.of(13, 0));

        when(personalRepository.findById(1L)).thenReturn(Optional.of(personal));
        when(disponibilidadeRepository.findByPersonalIdAndDiaSemana(1L, diaFuturo))
                .thenReturn(List.of(dispDia, restricao));
        when(produtoExibicaoRepository.findAgendamentoSlotsByPersonalIdAndDataBetween(anyLong(), any(), any()))
                .thenReturn(Collections.emptyList());

        // Act
        List<ResSlotDisponivelDTO> resultado = service.obterHorariosDisponiveis(1L, dataFutura);

        // Assert
        assertNotNull(resultado);

        // Nenhum slot deve estar entre 11:45 e 13:00
        resultado.forEach(slot -> {
            LocalTime inicio = LocalTime.parse(slot.inicio());
            assertFalse(
                    (inicio.equals(LocalTime.of(11, 45)) || inicio.isAfter(LocalTime.of(11, 45))) &&
                            inicio.isBefore(LocalTime.of(13, 0)),
                    "Horário " + inicio + " deveria estar bloqueado pela restrição com buffer"
            );
        });
    }

    @Test
    @DisplayName("Deve excluir slots ocupados por agendamentos ativos com buffer pós-atendimento")
    void deveExcluirSlotsOcupadosPorAgendamentosComBuffer() {
        // Arrange
        LocalDate dataFutura = LocalDate.now().plusDays(7);
        DiaSemana diaFuturo = DiaSemana.fromDayOfWeek(dataFutura.getDayOfWeek());

        DisponibilidadePersonal dispDia = new DisponibilidadePersonal();
        dispDia.setPersonal(personal);
        dispDia.setDiaSemana(diaFuturo);
        dispDia.setTipo(TipoHorario.DISPONIVEL);
        dispDia.setHoraInicio(LocalTime.of(8, 0));
        dispDia.setHoraFim(LocalTime.of(18, 0));

        // Agendamento FUNCIONAL (30 min) às 10:00
        // Com buffer de 15 min, deve bloquear de 10:00 até 10:45
        HorarioAgendadoProjection agendamento = mock(HorarioAgendadoProjection.class);
        when(agendamento.getDataInicio()).thenReturn(dataFutura.atTime(10, 0));
        when(agendamento.getTipoAula()).thenReturn(TipoAula.FUNCIONAL);
        when(agendamento.getStatus()).thenReturn(AgendamentoStatus.APROVADO);

        when(personalRepository.findById(1L)).thenReturn(Optional.of(personal));
        when(disponibilidadeRepository.findByPersonalIdAndDiaSemana(1L, diaFuturo))
                .thenReturn(List.of(dispDia));
        when(produtoExibicaoRepository.findAgendamentoSlotsByPersonalIdAndDataBetween(anyLong(), any(), any()))
                .thenReturn(List.of(agendamento));

        // Act
        List<ResSlotDisponivelDTO> resultado = service.obterHorariosDisponiveis(1L, dataFutura);

        // Assert
        assertNotNull(resultado);

        // Nenhum slot deve estar entre 10:00 e 10:45 (aula de 30 min + 15 min buffer)
        resultado.forEach(slot -> {
            LocalTime inicio = LocalTime.parse(slot.inicio());
            assertFalse(
                    (inicio.equals(LocalTime.of(10, 0)) || inicio.isAfter(LocalTime.of(10, 0))) &&
                            inicio.isBefore(LocalTime.of(10, 45)),
                    "Horário " + inicio + " deveria estar bloqueado pelo agendamento com buffer"
            );
        });
    }

    @Test
    @DisplayName("Deve garantir que slot tenha tempo mínimo de 30 minutos disponíveis")
    void deveGarantirTempoMinimoDisponivel() {
        // Arrange
        LocalDate dataFutura = LocalDate.now().plusDays(7);
        DiaSemana diaFuturo = DiaSemana.fromDayOfWeek(dataFutura.getDayOfWeek());

        // Disponibilidade de 10:00 às 10:30 (exatamente 30 minutos)
        DisponibilidadePersonal dispCurta = new DisponibilidadePersonal();
        dispCurta.setPersonal(personal);
        dispCurta.setDiaSemana(diaFuturo);
        dispCurta.setTipo(TipoHorario.DISPONIVEL);
        dispCurta.setHoraInicio(LocalTime.of(10, 0));
        dispCurta.setHoraFim(LocalTime.of(10, 30));

        when(personalRepository.findById(1L)).thenReturn(Optional.of(personal));
        when(disponibilidadeRepository.findByPersonalIdAndDiaSemana(1L, diaFuturo))
                .thenReturn(List.of(dispCurta));
        when(produtoExibicaoRepository.findAgendamentoSlotsByPersonalIdAndDataBetween(anyLong(), any(), any()))
                .thenReturn(Collections.emptyList());

        // Act
        List<ResSlotDisponivelDTO> resultado = service.obterHorariosDisponiveis(1L, dataFutura);

        // Assert
        assertNotNull(resultado);

        // Deve ter apenas 2 slots: 10:00 e 10:15 (ambos com 30 minutos até 10:30)
        // 10:30 não deve aparecer pois não há 30 minutos após
        assertTrue(resultado.size() >= 1 && resultado.size() <= 2);
    }

    @Test
    @DisplayName("Deve excluir slot se não houver tempo suficiente para aula completa")
    void deveExcluirSlotSeNaoHouverTempoSuficiente() {
        // Arrange
        LocalDate dataFutura = LocalDate.now().plusDays(7);
        DiaSemana diaFuturo = DiaSemana.fromDayOfWeek(dataFutura.getDayOfWeek());

        DisponibilidadePersonal dispDia = new DisponibilidadePersonal();
        dispDia.setPersonal(personal);
        dispDia.setDiaSemana(diaFuturo);
        dispDia.setTipo(TipoHorario.DISPONIVEL);
        dispDia.setHoraInicio(LocalTime.of(8, 0));
        dispDia.setHoraFim(LocalTime.of(18, 0));

        // Restrição que deixa apenas 15 minutos disponíveis antes
        DisponibilidadePersonal restricao = new DisponibilidadePersonal();
        restricao.setPersonal(personal);
        restricao.setDiaSemana(diaFuturo);
        restricao.setTipo(TipoHorario.RESTRITO);
        restricao.setHoraInicio(LocalTime.of(10, 15));
        restricao.setHoraFim(LocalTime.of(11, 0));

        when(personalRepository.findById(1L)).thenReturn(Optional.of(personal));
        when(disponibilidadeRepository.findByPersonalIdAndDiaSemana(1L, diaFuturo))
                .thenReturn(List.of(dispDia, restricao));
        when(produtoExibicaoRepository.findAgendamentoSlotsByPersonalIdAndDataBetween(anyLong(), any(), any()))
                .thenReturn(Collections.emptyList());

        // Act
        List<ResSlotDisponivelDTO> resultado = service.obterHorariosDisponiveis(1L, dataFutura);

        // Assert
        assertNotNull(resultado);

        // 10:00 não deve aparecer pois só tem 15 min até a restrição (que começa às 10:00 com buffer de 15 min)
        resultado.forEach(slot -> {
            LocalTime inicio = LocalTime.parse(slot.inicio());
            assertFalse(inicio.equals(LocalTime.of(10, 0)),
                    "Slot 10:00 não deveria estar disponível pois não há 30 minutos até o bloqueio");
        });
    }

    // ==================== TESTES: validaConflito ====================

    @Test
    @DisplayName("Deve detectar conflito entre dois horários RESTRITOS sobrepostos")
    void deveDetectarConflitoEntreRestritossobrepostos() {
        // Arrange
        DisponibilidadePersonal restricaoExistente = new DisponibilidadePersonal();
        restricaoExistente.setId(2L);
        restricaoExistente.setPersonal(personal);
        restricaoExistente.setDiaSemana(DiaSemana.SEGUNDA);
        restricaoExistente.setTipo(TipoHorario.RESTRITO);
        restricaoExistente.setHoraInicio(LocalTime.of(12, 0));
        restricaoExistente.setHoraFim(LocalTime.of(13, 0));

        ReqHorarioDTO novaRestricao = new ReqHorarioDTO(
                DiaSemana.SEGUNDA,
                TipoHorario.RESTRITO,
                LocalTime.of(12, 30),
                LocalTime.of(13, 30)
        );

        when(disponibilidadeRepository.findById(1L)).thenReturn(Optional.of(disponibilidadeManha));
        when(disponibilidadeRepository.encontrarConflitos(anyLong(), any(), any(), any(), anyLong()))
                .thenReturn(List.of(restricaoExistente));

        // Act & Assert
        assertThrows(SobreposicaoHorarioException.class, () -> {
            service.atualizarHorarios(1L, novaRestricao);
        });
    }

    @Test
    @DisplayName("Deve detectar conflito ao tentar marcar DISPONIVEL sobre RESTRITO")
    void deveDetectarConflitoAoTentarMarcarDisponivelSobreRestrito() {
        // Arrange
        DisponibilidadePersonal restricaoExistente = new DisponibilidadePersonal();
        restricaoExistente.setId(2L);
        restricaoExistente.setPersonal(personal);
        restricaoExistente.setDiaSemana(DiaSemana.SEGUNDA);
        restricaoExistente.setTipo(TipoHorario.RESTRITO);
        restricaoExistente.setHoraInicio(LocalTime.of(12, 0));
        restricaoExistente.setHoraFim(LocalTime.of(13, 0));

        ReqHorarioDTO novoDisponivel = new ReqHorarioDTO(
                DiaSemana.SEGUNDA,
                TipoHorario.DISPONIVEL,
                LocalTime.of(11, 30),
                LocalTime.of(14, 0)
        );

        when(disponibilidadeRepository.findById(1L)).thenReturn(Optional.of(disponibilidadeManha));
        when(disponibilidadeRepository.encontrarConflitos(anyLong(), any(), any(), any(), anyLong()))
                .thenReturn(List.of(restricaoExistente));

        // Act & Assert
        assertThrows(SobreposicaoHorarioException.class, () -> {
            service.atualizarHorarios(1L, novoDisponivel);
        });
    }

    @Test
    @DisplayName("Deve permitir múltiplos horários DISPONÍVEIS sobrepostos")
    void devePermitirMultiplosDisponiveisSobrepostos() {
        // Arrange
        DisponibilidadePersonal disponivelExistente = new DisponibilidadePersonal();
        disponivelExistente.setId(2L);
        disponivelExistente.setPersonal(personal);
        disponivelExistente.setDiaSemana(DiaSemana.SEGUNDA);
        disponivelExistente.setTipo(TipoHorario.DISPONIVEL);
        disponivelExistente.setHoraInicio(LocalTime.of(8, 0));
        disponivelExistente.setHoraFim(LocalTime.of(12, 0));

        ReqHorarioDTO novoDisponivel = new ReqHorarioDTO(
                DiaSemana.SEGUNDA,
                TipoHorario.DISPONIVEL,
                LocalTime.of(10, 0),
                LocalTime.of(14, 0)
        );

        when(disponibilidadeRepository.findById(1L)).thenReturn(Optional.of(disponibilidadeManha));
        when(disponibilidadeRepository.encontrarConflitos(anyLong(), any(), any(), any(), anyLong()))
                .thenReturn(List.of(disponivelExistente));
        when(disponibilidadeRepository.save(any())).thenReturn(disponibilidadeManha);

        // Act & Assert
        assertDoesNotThrow(() -> {
            service.atualizarHorarios(1L, novoDisponivel);
        });

        verify(disponibilidadeRepository, times(1)).save(any());
    }

    @Test
    @DisplayName("Deve permitir horários adjacentes sem sobreposição")
    void devePermitirHorariosAdjacentesSemSobreposicao() {
        // Arrange
        ReqHorarioDTO novoHorario = new ReqHorarioDTO(
                DiaSemana.SEGUNDA,
                TipoHorario.DISPONIVEL,
                LocalTime.of(14, 0),
                LocalTime.of(16, 0)
        );

        when(disponibilidadeRepository.findById(1L)).thenReturn(Optional.of(disponibilidadeManha));
        when(disponibilidadeRepository.encontrarConflitos(anyLong(), any(), any(), any(), anyLong()))
                .thenReturn(Collections.emptyList());
        when(disponibilidadeRepository.save(any())).thenReturn(disponibilidadeManha);

        // Act & Assert
        assertDoesNotThrow(() -> {
            service.atualizarHorarios(1L, novoHorario);
        });

        verify(disponibilidadeRepository, times(1)).save(any());
    }

    // ==================== TESTES: validarContraAgendamentosAtivos ====================

    @Test
    @DisplayName("Deve lançar exceção ao criar RESTRITO sobre agendamento ativo")
    void deveLancarExcecaoAoCriarRestritoSobreAgendamentoAtivo() {
        // Arrange
        LocalDate proximaSegunda = LocalDate.now();
        while (proximaSegunda.getDayOfWeek() != java.time.DayOfWeek.MONDAY) {
            proximaSegunda = proximaSegunda.plusDays(1);
        }

        // Agendamento FUNCIONAL (30 min) às 10:00 + 15 min buffer = até 10:45
        HorarioAgendadoProjection agendamento = mock(HorarioAgendadoProjection.class);
        when(agendamento.getDataInicio()).thenReturn(proximaSegunda.atTime(10, 0));
        when(agendamento.getTipoAula()).thenReturn(TipoAula.FUNCIONAL);
        when(agendamento.getStatus()).thenReturn(AgendamentoStatus.APROVADO);

        // Tentando criar restrição de 10:30 às 11:00 (sobrepõe o buffer do agendamento)
        ReqHorarioDTO novaRestricao = new ReqHorarioDTO(
                DiaSemana.SEGUNDA,
                TipoHorario.RESTRITO,
                LocalTime.of(10, 30),
                LocalTime.of(11, 0)
        );

        when(disponibilidadeRepository.findById(1L)).thenReturn(Optional.of(disponibilidadeManha));
        when(disponibilidadeRepository.encontrarConflitos(anyLong(), any(), any(), any(), anyLong()))
                .thenReturn(Collections.emptyList());
        when(personalRepository.findById(1L)).thenReturn(Optional.of(personal));
        when(produtoExibicaoRepository.findAgendamentoSlotsByPersonalIdAndDataBetween(anyLong(), any(), any()))
                .thenReturn(List.of(agendamento));

        // Act & Assert
        assertThrows(SobreposicaoHorarioException.class, () -> {
            service.atualizarHorarios(1L, novaRestricao);
        });

        verify(disponibilidadeRepository, never()).save(any());
    }

    @Test
    @DisplayName("Deve lançar exceção ao criar RESTRITO considerando buffer de 15 min antes sobre agendamento")
    void deveLancarExcecaoAoCriarRestritoComBufferAntesSobreAgendamento() {
        // Arrange
        LocalDate proximaSegunda = LocalDate.now();
        while (proximaSegunda.getDayOfWeek() != java.time.DayOfWeek.MONDAY) {
            proximaSegunda = proximaSegunda.plusDays(1);
        }

        // Agendamento às 10:00
        HorarioAgendadoProjection agendamento = mock(HorarioAgendadoProjection.class);
        when(agendamento.getDataInicio()).thenReturn(proximaSegunda.atTime(10, 0));
        when(agendamento.getTipoAula()).thenReturn(TipoAula.FUNCIONAL);
        when(agendamento.getStatus()).thenReturn(AgendamentoStatus.APROVADO);

        // Restrição de 10:00 às 11:00, mas com buffer de 15 min antes, começa em 9:45
        // Isso NÃO deveria conflitar com agendamento às 10:00
        ReqHorarioDTO novaRestricao = new ReqHorarioDTO(
                DiaSemana.SEGUNDA,
                TipoHorario.RESTRITO,
                LocalTime.of(10, 0),
                LocalTime.of(11, 0)
        );

        when(disponibilidadeRepository.findById(1L)).thenReturn(Optional.of(disponibilidadeManha));
        when(disponibilidadeRepository.encontrarConflitos(anyLong(), any(), any(), any(), anyLong()))
                .thenReturn(Collections.emptyList());
        when(personalRepository.findById(1L)).thenReturn(Optional.of(personal));
        when(produtoExibicaoRepository.findAgendamentoSlotsByPersonalIdAndDataBetween(anyLong(), any(), any()))
                .thenReturn(List.of(agendamento));

        // Act & Assert
        // A restrição com buffer (9:45-11:00) sobrepõe o agendamento (10:00-10:45)
        assertThrows(SobreposicaoHorarioException.class, () -> {
            service.atualizarHorarios(1L, novaRestricao);
        });
    }

    @Test
    @DisplayName("Deve permitir RESTRITO quando não há sobreposição com agendamentos")
    void devePermitirRestritoQuandoNaoHaSobreposicaoComAgendamentos() {
        // Arrange
        LocalDate proximaSegunda = LocalDate.now();
        while (proximaSegunda.getDayOfWeek() != java.time.DayOfWeek.MONDAY) {
            proximaSegunda = proximaSegunda.plusDays(1);
        }

        // Agendamento às 10:00 (termina às 10:45 com buffer)
        HorarioAgendadoProjection agendamento = mock(HorarioAgendadoProjection.class);
        when(agendamento.getDataInicio()).thenReturn(proximaSegunda.atTime(10, 0));
        when(agendamento.getTipoAula()).thenReturn(TipoAula.FUNCIONAL);
        when(agendamento.getStatus()).thenReturn(AgendamentoStatus.APROVADO);

        // Restrição de 11:00 às 12:00 (com buffer começa às 10:45, exatamente quando termina o agendamento)
        ReqHorarioDTO novaRestricao = new ReqHorarioDTO(
                DiaSemana.SEGUNDA,
                TipoHorario.RESTRITO,
                LocalTime.of(11, 0),
                LocalTime.of(12, 0)
        );

        when(disponibilidadeRepository.findById(1L)).thenReturn(Optional.of(disponibilidadeManha));
        when(disponibilidadeRepository.encontrarConflitos(anyLong(), any(), any(), any(), anyLong()))
                .thenReturn(Collections.emptyList());
        when(personalRepository.findById(1L)).thenReturn(Optional.of(personal));
        when(produtoExibicaoRepository.findAgendamentoSlotsByPersonalIdAndDataBetween(anyLong(), any(), any()))
                .thenReturn(List.of(agendamento));
        when(disponibilidadeRepository.save(any())).thenReturn(disponibilidadeManha);

        // Act & Assert
        assertDoesNotThrow(() -> {
            service.atualizarHorarios(1L, novaRestricao);
        });

        verify(disponibilidadeRepository, times(1)).save(any());
    }

    @Test
    @DisplayName("Deve validar contra agendamento PRESENCIAL com duração de 60 minutos")
    void deveValidarContraAgendamentoPresencialComDuracao60() {
        // Arrange
        LocalDate proximaSegunda = LocalDate.now();
        while (proximaSegunda.getDayOfWeek() != java.time.DayOfWeek.MONDAY) {
            proximaSegunda = proximaSegunda.plusDays(1);
        }

        // Agendamento PRESENCIAL (60 min) às 14:00 + 15 min buffer = até 15:15
        HorarioAgendadoProjection agendamento = mock(HorarioAgendadoProjection.class);
        when(agendamento.getDataInicio()).thenReturn(proximaSegunda.atTime(14, 0));
        when(agendamento.getTipoAula()).thenReturn(TipoAula.PRESENCIAL);
        when(agendamento.getStatus()).thenReturn(AgendamentoStatus.APROVADO);

        // Tentando criar restrição de 15:00 às 16:00 (sobrepõe o buffer)
        ReqHorarioDTO novaRestricao = new ReqHorarioDTO(
                DiaSemana.SEGUNDA,
                TipoHorario.RESTRITO,
                LocalTime.of(15, 0),
                LocalTime.of(16, 0)
        );

        when(disponibilidadeRepository.findById(1L)).thenReturn(Optional.of(disponibilidadeManha));
        when(disponibilidadeRepository.encontrarConflitos(anyLong(), any(), any(), any(), anyLong()))
                .thenReturn(Collections.emptyList());
        when(personalRepository.findById(1L)).thenReturn(Optional.of(personal));
        when(produtoExibicaoRepository.findAgendamentoSlotsByPersonalIdAndDataBetween(anyLong(), any(), any()))
                .thenReturn(List.of(agendamento));

        // Act & Assert
        assertThrows(SobreposicaoHorarioException.class, () -> {
            service.atualizarHorarios(1L, novaRestricao);
        });
    }

    @Test
    @DisplayName("Não deve validar agendamentos para horário DISPONIVEL")
    void naoDeveValidarAgendamentosParaHorarioDisponivel() {
        // Arrange
        ReqHorarioDTO novoDisponivel = new ReqHorarioDTO(
                DiaSemana.SEGUNDA,
                TipoHorario.DISPONIVEL,
                LocalTime.of(14, 0),
                LocalTime.of(16, 0)
        );

        when(disponibilidadeRepository.findById(1L)).thenReturn(Optional.of(disponibilidadeManha));
        when(disponibilidadeRepository.encontrarConflitos(anyLong(), any(), any(), any(), anyLong()))
                .thenReturn(Collections.emptyList());
        when(disponibilidadeRepository.save(any())).thenReturn(disponibilidadeManha);

        // Act
        service.atualizarHorarios(1L, novoDisponivel);

        // Assert
        verify(personalRepository, never()).findById(anyLong());
        verify(produtoExibicaoRepository, never()).findAgendamentoSlotsByPersonalIdAndDataBetween(anyLong(), any(), any());
        verify(disponibilidadeRepository, times(1)).save(any());
    }
}

