package com.spring.ApiSystem.domain.disponibilidade;

import com.spring.ApiSystem.domain.disponibilidade.enums.DiaSemana;
import com.spring.ApiSystem.domain.disponibilidade.enums.TipoHorario;
import com.spring.ApiSystem.domain.disponibilidade.dto.request.ReqHorarioDTO;
import com.spring.ApiSystem.domain.disponibilidade.dto.response.ResHorarioDTO;
import com.spring.ApiSystem.domain.disponibilidade.dto.response.ResSlotDisponivelDTO;
import com.spring.ApiSystem.domain.disponibilidade.exception.SobreposicaoHorarioException;
import com.spring.ApiSystem.domain.disponibilidade.exception.HorarioInvalidoException;

import com.spring.ApiSystem.domain.personal.Personal;
import com.spring.ApiSystem.domain.personal.PersonalRepository;
import com.spring.ApiSystem.domain.personal.PersonalService;
import com.spring.ApiSystem.domain.personal.exception.PersonalNaoExisteExcepetion;
import com.spring.ApiSystem.domain.produtoexibicao.ProdutoExibicaoRepository;
import java.time.temporal.ChronoUnit;
import com.spring.ApiSystem.domain.produtoexibicao.enums.TipoAula;
import com.spring.ApiSystem.domain.usuario.exception.NaoAutorizadoException;
import com.spring.ApiSystem.domain.usuario.security.JpaUserDetailsService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class DisponibilidadePersonalService {


    private static final int BUFFER_ANTECEDENCIA_RESTRICAO = 15;
    private static final int DURACAO_MINIMA_AULA = 30;
    private static final int DURACAO_MINIMA_DISPONIBILIDADE = 60;
    private static final int DURACAO_MAXIMA_PERIODO = 720;
    private static final LocalTime HORARIO_COMERCIAL_INICIO = LocalTime.of(6, 0);
    private static final LocalTime HORARIO_COMERCIAL_FIM = LocalTime.of(23, 0);
    private static final int DIAS_VALIDACAO_AGENDAMENTOS = 30;

    private final DisponibilidadePersonalRepository disponibilidadeRepository;
    private final PersonalRepository personalRepository;
    private final ProdutoExibicaoRepository produtoExibicaoRepository;
    private final JpaUserDetailsService detailsService;

    public DisponibilidadePersonalService(DisponibilidadePersonalRepository disponibilidadeRepository, PersonalRepository personalRepository, ProdutoExibicaoRepository produtoExibicaoRepository, JpaUserDetailsService detailsService) {
        this.disponibilidadeRepository = disponibilidadeRepository;
        this.personalRepository = personalRepository;
        this.produtoExibicaoRepository = produtoExibicaoRepository;
        this.detailsService = detailsService;
    }

    private boolean intervalsOverlap(LocalDateTime aStart, LocalDateTime aEnd, LocalDateTime bStart, LocalDateTime bEnd) {
        // Sobreposição ocorre se o fim de um intervalo não é anterior ao início do outro,
        // E o fim do segundo não é anterior ao início do primeiro.
        return aStart.isBefore(bEnd) && aEnd.isAfter(bStart);
    }

    /**
     * Valida todas as regras de negócio para criação/atualização de horários
     */
    private void validarHorario(Personal personal, DiaSemana diaSemana, LocalTime horaInicio, LocalTime horaFim,
                                Long horarioId, TipoHorario tipo) {


        if (horaFim.isBefore(horaInicio) || horaFim.equals(horaInicio)) {
            throw new HorarioInvalidoException("Hora fim deve ser posterior à hora início");
        }

        if (horaInicio.isBefore(HORARIO_COMERCIAL_INICIO) || horaFim.isAfter(HORARIO_COMERCIAL_FIM)) {
            throw new HorarioInvalidoException(
                String.format("Horários devem estar entre %s e %s", HORARIO_COMERCIAL_INICIO, HORARIO_COMERCIAL_FIM)
            );
        }

        long duracaoMinutos = ChronoUnit.MINUTES.between(horaInicio, horaFim);

        if (tipo == TipoHorario.DISPONIVEL && duracaoMinutos < DURACAO_MINIMA_DISPONIBILIDADE) {
            throw new HorarioInvalidoException(
                String.format("Período de disponibilidade deve ter no mínimo %d minutos", DURACAO_MINIMA_DISPONIBILIDADE)
            );
        }

        if (duracaoMinutos > DURACAO_MAXIMA_PERIODO) {
            throw new HorarioInvalidoException(
                String.format("Período não pode exceder %d minutos (12 horas)", DURACAO_MAXIMA_PERIODO)
            );
        }

        if (tipo == TipoHorario.RESTRITO) {
            validarRestricaoDentroDeDisponibilidade(personal, diaSemana, horaInicio, horaFim, horarioId);
        }

        validarConflito(personal, diaSemana, horaInicio, horaFim, horarioId, tipo);

        if (tipo == TipoHorario.RESTRITO) {
            validarContraAgendamentosFuturos(personal, diaSemana, horaInicio, horaFim);
        }
    }

    /**
     * Valida se uma restrição está dentro de um período de disponibilidade
     */
    private void validarRestricaoDentroDeDisponibilidade(Personal personal, DiaSemana diaSemana,
                                                         LocalTime horaInicio, LocalTime horaFim, Long horarioId) {
        List<DisponibilidadePersonal> disponibilidades = disponibilidadeRepository
            .findByPersonalIdAndDiaSemana(personal.getId(), diaSemana)
            .stream()
            .filter(d -> d.getTipo() == TipoHorario.DISPONIVEL)
            .filter(d -> horarioId == null || !d.getId().equals(horarioId))
            .toList();

        boolean dentroDeDisponibilidade = disponibilidades.stream()
            .anyMatch(d -> !horaInicio.isBefore(d.getHoraInicio()) && !horaFim.isAfter(d.getHoraFim()));

        if (!dentroDeDisponibilidade) {
            throw new HorarioInvalidoException(
                "Restrição deve estar completamente dentro de um período de disponibilidade"
            );
        }
    }

    /**
     * Valida conflitos com agendamentos ativos nos próximos 30 dias
     */
    private void validarContraAgendamentosFuturos(Personal personal, DiaSemana diaSemana,
                                                   LocalTime horaInicio, LocalTime horaFim) {

        final int bufferPosAtendimento = Optional.ofNullable(personal.getBufferMinutos()).orElse(15);
        LocalTime restritoInicioComBuffer = horaInicio.minusMinutes(BUFFER_ANTECEDENCIA_RESTRICAO);

        LocalDate hoje = LocalDate.now();
        LocalDate dataFim = hoje.plusDays(DIAS_VALIDACAO_AGENDAMENTOS);

        // Valida para todas as ocorrências do dia da semana nos próximos 30 dias
        List<HorarioAgendadoProjection> todosAgendamentos = produtoExibicaoRepository.findAgendamentoSlotsByPersonalIdAndDataBetween(
                personal.getId(),
                hoje.atStartOfDay(),
                dataFim.atTime(23, 59, 59)
        );

        DayOfWeek diaSemanaAlvo = diaSemana.getDayOfWeek();

        for (HorarioAgendadoProjection slot : todosAgendamentos){
            LocalDateTime agendamentoStart = slot.getDataInicio();

            if (agendamentoStart.getDayOfWeek() != diaSemanaAlvo){
                continue;
            }

            LocalDate dataAgendamento = agendamentoStart.toLocalDate();
            LocalDateTime inicioValidacao = dataAgendamento.atTime(restritoInicioComBuffer);
            LocalDateTime fimValidacao = dataAgendamento.atTime(horaFim);

            int duracao = TipoAula.FUNCIONAL == slot.getTipoAula() ? 30 : 60;
            LocalDateTime agendamentoEnd = agendamentoStart
                    .plusMinutes(duracao)
                    .plusMinutes(bufferPosAtendimento);

            if (intervalsOverlap(agendamentoStart, agendamentoEnd, inicioValidacao, fimValidacao)) {
                throw new SobreposicaoHorarioException();
            }
        }
    }


    @Transactional
    public void criarDisponibilidadePadrao(Long personalId) {
        Personal personal = personalRepository.findById(personalId)
                .orElseThrow(PersonalNaoExisteExcepetion::new);

        List<DisponibilidadePersonal> defaults = new ArrayList<>();
        for (DiaSemana dia : DiaSemana.values()) {
            defaults.add(new DisponibilidadePersonal(personal, dia, TipoHorario.DISPONIVEL, LocalTime.of(8, 0), LocalTime.of(18, 0)));
            defaults.add(new DisponibilidadePersonal(personal, dia, TipoHorario.RESTRITO, LocalTime.of(12, 0), LocalTime.of(13, 0)));
        }

        disponibilidadeRepository.saveAll(defaults);
    }

    // Atualização dos horarios
    @Transactional
    public ResHorarioDTO atualizarHorarios(Personal personal, Long horarioId, ReqHorarioDTO request) {

        DisponibilidadePersonal horarioExistente = disponibilidadeRepository.findById(horarioId)
                .orElseThrow(() -> new EntityNotFoundException("Horário não encontrado"));

        if(!horarioExistente.getPersonal().getId().equals(personal.getId())){
            throw new NaoAutorizadoException();
        }

        // Valida ANTES de atualizar
        validarHorario(
                personal,
                request.diaSemana(),
                request.horaInicio(),
                request.horaFim(),
                horarioId,
                request.tipo()
        );

        horarioExistente.setDiaSemana(request.diaSemana());
        horarioExistente.setTipo(request.tipo());
        horarioExistente.setHoraInicio(request.horaInicio());
        horarioExistente.setHoraFim(request.horaFim());

        return new ResHorarioDTO(disponibilidadeRepository.saveAndFlush(horarioExistente));
    }

    @Transactional(readOnly = true)
    public List<ResSlotDisponivelDTO> obterHorariosDisponiveis(Personal personal, LocalDate dataDesejada, TipoAula tipoAula) {


        final int bufferPosAtendimento = Optional.ofNullable(personal.getBufferMinutos()).orElse(15);
        final int duracaoAulaNecessaria = TipoAula.FUNCIONAL == tipoAula ? 30 : 60;

        LocalTime horaCorte = LocalTime.MIN;
        LocalDate hoje = LocalDate.now();

        // Aplica corte para hoje e para o próximo dia (24h a partir de agora)
        if (dataDesejada.isEqual(hoje) || dataDesejada.isEqual(hoje.plusDays(1))) {
            LocalDateTime ref = LocalDateTime.now();
            if (dataDesejada.isEqual(hoje.plusDays(1))) {
                ref = ref.plusDays(1);
            }

            int resto = ref.getMinute() % 15;
            int minutosParaAcrescentar = (resto == 0) ? 15 : 15 - resto;

            LocalDateTime corteDateTime = ref.plusMinutes(minutosParaAcrescentar).withSecond(0).withNano(0);
            horaCorte = corteDateTime.toLocalTime();
        }

        DayOfWeek diaSemana = dataDesejada.getDayOfWeek();
        List<DisponibilidadePersonal> disponibilidade = disponibilidadeRepository
                .findByPersonalIdAndDiaSemana(personal.getId(), DiaSemana.fromDayOfWeek(diaSemana));

        if (disponibilidade.isEmpty()) {
            return Collections.emptyList();
        }

        Set<LocalTime> horariosBloqueados = new HashSet<>();
        List<DisponibilidadePersonal> blocosDisponibilidade = new ArrayList<>();

        // Processamento de Restrições (com 15 min de antecedência)
        for (DisponibilidadePersonal disp : disponibilidade) {
            if (disp.getTipo() == TipoHorario.DISPONIVEL) {
                blocosDisponibilidade.add(disp);
            } else {
                LocalTime inicio = disp.getHoraInicio().minusMinutes(BUFFER_ANTECEDENCIA_RESTRICAO);
                LocalTime fim = disp.getHoraFim();
                LocalTime current = inicio;

                while (current.isBefore(fim)) {
                    horariosBloqueados.add(current);
                    current = current.plusMinutes(15);
                }
            }
        }

        // Processamento de Agendamentos ativos (com o tempo do buffer pós-atendimento)
        LocalDateTime startOfDay = dataDesejada.atStartOfDay();
        LocalDateTime endOfDay = dataDesejada.atTime(23, 59, 59);

        List<HorarioAgendadoProjection> agendamentos = produtoExibicaoRepository
                .findAgendamentoSlotsByPersonalIdAndDataBetween(personal.getId(), startOfDay, endOfDay);

        for (HorarioAgendadoProjection slot : agendamentos) {
            LocalDateTime inicioAula = slot.getDataInicio();
            TipoAula tipoAulaAgendada = slot.getTipoAula();
            int duracaoMinutos = TipoAula.FUNCIONAL == tipoAulaAgendada ? 30 : 60;

            LocalDateTime fimBloqueio = inicioAula.plusMinutes(duracaoMinutos).plusMinutes(bufferPosAtendimento);

            LocalDateTime current = inicioAula;
            // Bloqueia todos os slots desde o início até antes do fimBloqueio
            // Depois, bloqueia também os slots que não teriam tempo suficiente para uma aula completa
            while (current.isBefore(fimBloqueio)) {
                horariosBloqueados.add(current.toLocalTime());
                current = current.plusMinutes(15);
            }

            // Bloqueia slots insuficientes entre o último slot bloqueado e o próximo intervalo de 15min
            LocalTime ultimoSlotBloqueado = current.toLocalTime();
            LocalTime proximoSlotCompleto = current.plusMinutes(15).toLocalTime();
            long minutosRestantes = ChronoUnit.MINUTES.between(ultimoSlotBloqueado, proximoSlotCompleto);

            if (minutosRestantes < DURACAO_MINIMA_AULA) {
                horariosBloqueados.add(ultimoSlotBloqueado);
            }

        }
        // Filtragem dos horarios final
        List<LocalTime> slotsFinais = new ArrayList<>();

        for (DisponibilidadePersonal bloco : blocosDisponibilidade) {
            LocalTime inicioBloco = bloco.getHoraInicio();
            LocalTime fimBloco = bloco.getHoraFim();

            // Aplica o corte para hoje e para amanhã
            LocalTime current = ((dataDesejada.isEqual(hoje) || dataDesejada.isEqual(hoje.plusDays(1)))
                    && inicioBloco.isBefore(horaCorte)) ? horaCorte : inicioBloco;

            while (current.isBefore(fimBloco)) {

                if (horariosBloqueados.contains(current)) {
                    current = current.plusMinutes(15);
                    continue;
                }

                LocalTime proximoBloqueio = encontrarProximoBloqueio(current, fimBloco, horariosBloqueados);

                // Calcula minutos disponíveis
                long minutosDisponiveis = ChronoUnit.MINUTES.between(current, proximoBloqueio);

                // Verifica se há tempo suficiente para a aula completa sem ultrapassar o fim do bloco
                LocalTime fimAulaPrevisto = current.plusMinutes(duracaoAulaNecessaria);

                // A aula deve terminar ANTES do horário de fim da disponibilidade (nesse caso não termina exatamente no limite)
                if (minutosDisponiveis >= duracaoAulaNecessaria && fimAulaPrevisto.isBefore(fimBloco)) {
                    slotsFinais.add(current);
                } else {
                    horariosBloqueados.add(current);
                }

                current = current.plusMinutes(15);
            }
        }

        return slotsFinais.stream()
                .sorted(LocalTime::compareTo)
                .map(slotsInicio -> new ResSlotDisponivelDTO(slotsInicio, slotsInicio.plusMinutes(15)))
                .collect(Collectors.toList());
    }


    // Esse metodo serve pra encontrar o próximo bloqueio a partir de um horário inicial dentro de um limite.
    private LocalTime encontrarProximoBloqueio(LocalTime start, LocalTime limite, Set<LocalTime> bloqueios) {
        LocalTime next = start.plusMinutes(15);
        while (next.isBefore(limite) || next.equals(limite)) {
            if (bloqueios.contains(next)) {
                return next;
            }
            next = next.plusMinutes(15);
        }
        return limite;
    }


    private void validarConflito(Personal personal, DiaSemana diaSemana, LocalTime horaInicio, LocalTime horaFim, Long horarioId, TipoHorario tipo) {

        List<DisponibilidadePersonal> sobrepostos = disponibilidadeRepository.encontrarConflitos(
                personal.getId(), diaSemana, horaInicio, horaFim, horarioId
        );

        // Valida conflitos com horários existentes
        for (DisponibilidadePersonal sobreposto : sobrepostos) {
//            if (tipo == TipoHorario.DISPONIVEL && sobreposto.getTipo() == TipoHorario.DISPONIVEL) {
//                throw new SobreposicaoHorarioException();
//            }

            if (tipo == TipoHorario.RESTRITO && sobreposto.getTipo() == TipoHorario.RESTRITO) {
                throw new SobreposicaoHorarioException();
            }
        }

        if (tipo == TipoHorario.RESTRITO) {
            LocalTime restritoInicioComBuffer = horaInicio.minusMinutes(BUFFER_ANTECEDENCIA_RESTRICAO);

            LocalDateTime novoPeriodoStart = LocalDate.now().atTime(restritoInicioComBuffer);
            LocalDateTime novoPeriodoEnd = LocalDate.now().atTime(horaFim);

            validarContraAgendamentosAtivos(personal, diaSemana, novoPeriodoStart, novoPeriodoEnd);
        }
    }

    /**
     * Valida se o 'novo período' (Restrição) sobrepõe qualquer agendamento ATIVO + INTERVALO PÓS-ATENDIMENTO.
     */
    private void validarContraAgendamentosAtivos(Personal personal, DiaSemana diaSemana, LocalDateTime novoPeriodoStart, LocalDateTime novoPeriodoEnd) {
        final int bufferPosAtendimento = Optional.ofNullable(personal.getBufferMinutos()).orElse(15);

        // Encontra a próxima ocorrência do dia da semana (para buscar agendamentos)
        LocalDate dataRef = LocalDate.now();
        while (dataRef.getDayOfWeek() != diaSemana.getDayOfWeek()) {
            dataRef = dataRef.plusDays(1);
        }

        LocalDateTime start = dataRef.atStartOfDay();
        LocalDateTime end = dataRef.atTime(23,59,59);


        List<HorarioAgendadoProjection> agendamentos = produtoExibicaoRepository
                .findAgendamentoSlotsByPersonalIdAndDataBetween(personal.getId(), start, end);

        // Ajusta o novo período de restrição para a data de referência
        LocalDateTime novoRestricaoStart = dataRef.atTime(novoPeriodoStart.toLocalTime());
        LocalDateTime novoRestricaoEnd = dataRef.atTime(novoPeriodoEnd.toLocalTime());

        for (HorarioAgendadoProjection slot : agendamentos) {
            LocalDateTime agendamentoStart = slot.getDataInicio();
            TipoAula tipoAula = slot.getTipoAula();
            int duracao = TipoAula.FUNCIONAL == tipoAula ? 30 : 60;

            // Calculo do tempo bloqueado pelo agendamento (Aula + Buffer Pós-Atendimento)
            LocalDateTime agendamentoEnd = agendamentoStart.plusMinutes(duracao).plusMinutes(bufferPosAtendimento);

            if (intervalsOverlap(agendamentoStart, agendamentoEnd, novoRestricaoStart, novoRestricaoEnd)) {
                throw new SobreposicaoHorarioException();
            }
        }
    }

    public List<DisponibilidadePersonal> pegarCronograma(Personal personal) {
        return disponibilidadeRepository.findByPersonal(personal);
    }
}

