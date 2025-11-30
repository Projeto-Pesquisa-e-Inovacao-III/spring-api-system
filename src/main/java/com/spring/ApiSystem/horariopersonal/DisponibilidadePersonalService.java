package com.spring.ApiSystem.horariopersonal;

import com.spring.ApiSystem.agendamento.dto.response.HorarioAgendadoProjectionDto;
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

    private final DisponibilidadePersonalRepository disponibilidadeRepository;
    private final PersonalRepository personalRepository;
    private final ProdutoExibicaoRepository produtoExibicaoRepository;

    public DisponibilidadePersonalService(DisponibilidadePersonalRepository disponibilidadeRepository, PersonalRepository personalRepository, ProdutoExibicaoRepository produtoExibicaoRepository) {
        this.disponibilidadeRepository = disponibilidadeRepository;
        this.personalRepository = personalRepository;
        this.produtoExibicaoRepository = produtoExibicaoRepository;
    }

    private boolean intervalsOverlap(LocalDateTime aStart, LocalDateTime aEnd, LocalDateTime bStart, LocalDateTime bEnd) {
        // Sobremposição ocorre se o fim de um intervalo não é anterior ao início do outro,
        // E o fim do segundo não é anterior ao início do primeiro.
        return aStart.isBefore(bEnd) && aEnd.isAfter(bStart);
    }


    @Transactional
    public void criarDisponibilidadePadrao(Long personalId) {
        Personal personal = personalRepository.findById(personalId)
                .orElseThrow(PersonalNaoExisteExcepetion::new);

        List<DisponibilidadePersonal> defaults = new ArrayList<>();
        for (DiaSemana dia : DiaSemana.values()) {
            // DISPONIVEL 08:00-18:00
            defaults.add(new DisponibilidadePersonal(personal, dia, TipoHorario.DISPONIVEL, LocalTime.of(8, 0), LocalTime.of(18, 0)));

            // RESTRITO 12:00-13:00
            // O bloqueio de 15 min antes será aplicado na leitura (obterHorariosDisponiveis) e na validação.
            defaults.add(new DisponibilidadePersonal(personal, dia, TipoHorario.RESTRITO, LocalTime.of(12, 0), LocalTime.of(13, 0)));
        }

        disponibilidadeRepository.saveAll(defaults);
    }



    // Atualização dos horarios
    @Transactional
    public ResHorarioDTO atualizarHorarios(Long horarioId, ReqHorarioDTO request) {

        DisponibilidadePersonal horarioExistente = disponibilidadeRepository.findById(horarioId)
                .orElseThrow(() -> new EntityNotFoundException("Horário não encontrado"));

        Long personalId = horarioExistente.getPersonal().getId();

        horarioExistente.setDiaSemana(request.diaSemana());
        horarioExistente.setTipo(request.tipo());
        horarioExistente.setHoraInicio(request.horaInicio());
        horarioExistente.setHoraFim(request.horaFim());

        validarConflito(
                personalId,
                request.diaSemana(),
                request.horaInicio(),
                request.horaFim(),
                horarioId,
                request.tipo()
        );

        return new ResHorarioDTO(disponibilidadeRepository.saveAndFlush(horarioExistente));
    }



    @Transactional(readOnly = true)
    public List<ResSlotDisponivelDTO> obterHorariosDisponiveis(Long personalId, LocalDate dataDesejada) {

        Personal personal = personalRepository.findById(personalId)
                .orElseThrow(PersonalNaoExisteExcepetion::new);


        final int bufferPosAtendimento = Optional.ofNullable(personal.getBufferMinutos()).orElse(15);

        LocalTime horaCorte = LocalTime.MIN;

        if (dataDesejada.isEqual(LocalDate.now())) {
            LocalTime now = LocalTime.now();

            // Calcula quantos minutos faltam para o próximo slot de 15 minutos.
            int minutosParaAcrescentar = 15 - (now.getMinute() % 15);

            if (minutosParaAcrescentar == 15) {
                horaCorte = now.plusMinutes(15).withSecond(0).withNano(0);
            } else {
                horaCorte = now.plusMinutes(minutosParaAcrescentar).withSecond(0).withNano(0);
            }
        }

        DayOfWeek diaSemana = dataDesejada.getDayOfWeek();
        List<DisponibilidadePersonal> disponibilidade = disponibilidadeRepository
                .findByPersonalIdAndDiaSemana(personalId, DiaSemana.fromDayOfWeek(diaSemana));

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
                // Aplica o buffer de 15 minutos antes do início da restrição
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
                .findAgendamentoSlotsByPersonalIdAndDataBetween(personalId, startOfDay, endOfDay);

        for (HorarioAgendadoProjection slot : agendamentos) {
            LocalDateTime inicioAula = slot.getDataInicio();
            int duracaoMinutos = TipoAula.FUNCIONAL == slot.getTipoAula() ? 30 : 60;

            // [Início da Aula] até [Fim da Aula + Buffer Pós-Atendimento]
            LocalDateTime fimBloqueio = inicioAula.plusMinutes(duracaoMinutos).plusMinutes(bufferPosAtendimento);

            LocalDateTime current = inicioAula;

            while (current.isBefore(fimBloqueio)) {
                horariosBloqueados.add(current.toLocalTime());
                current = current.plusMinutes(15);
            }
        }

        // Filtragem dos horarios final
        List<LocalTime> slotsFinais = new ArrayList<>();

        for (DisponibilidadePersonal bloco : blocosDisponibilidade) {
            LocalTime inicioBloco = bloco.getHoraInicio();
            LocalTime fimBloco = bloco.getHoraFim();

            // Ponto de partida do loop (considera o horário de corte do dia atual usando o LocaDate.now())
            LocalTime current = (dataDesejada.isEqual(LocalDate.now()) && inicioBloco.isBefore(horaCorte))
                    ? horaCorte : inicioBloco;

            while (current.isBefore(fimBloco)) {

               // Se o Slot já está ocupado por Agendamento/Restrição
                if (horariosBloqueados.contains(current)) {
                    current = current.plusMinutes(15);
                    continue;
                }

                // Tempo Hábil (Verifica se o tempo restante permite a duração mínima da aula)
                LocalTime proximoBloqueio = encontrarProximoBloqueio(current, fimBloco, horariosBloqueados);

                long minutosDisponiveis = java.time.temporal.ChronoUnit.MINUTES.between(current, proximoBloqueio);

                if (minutosDisponiveis < DURACAO_MINIMA_AULA) {
                    horariosBloqueados.add(current);
                } else {
                    slotsFinais.add(current);
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
        // Retorna o limite do bloco se não encontrar bloqueios internos.
    }


    private void validarConflito(Long personalId, DiaSemana diaSemana, LocalTime horaInicio, LocalTime horaFim, Long horarioId, TipoHorario tipo) {

        List<DisponibilidadePersonal> sobrepostos = disponibilidadeRepository.encontrarConflitos(
                personalId, diaSemana, horaInicio, horaFim, horarioId
        );

        if (!sobrepostos.isEmpty()) {

            boolean temConflito = sobrepostos.stream()
                    .anyMatch(existente -> {
                        TipoHorario existenteTipo = existente.getTipo();

                        // Bloco não-disponível (RESTRITO) sobrepondo qualquer bloco não-disponível.
                        if ((tipo == TipoHorario.RESTRITO) && (existenteTipo == TipoHorario.RESTRITO)) {
                            return true;
                        }

                        // Isto impede que o Personal marque como "Disponível" um horário que ele já marcou como restrito.
                        if (tipo == TipoHorario.DISPONIVEL &&
                                (existenteTipo == TipoHorario.RESTRITO)) {
                            return true;
                        }

                        return false;
                    });

            if (temConflito) {
                throw new SobreposicaoHorarioException();
            }
        }

        if (tipo == TipoHorario.RESTRITO) {

            LocalTime restritoInicioComBuffer = horaInicio.minusMinutes(BUFFER_ANTECEDENCIA_RESTRICAO);

            LocalDateTime novoPeriodoStart = LocalDate.now().atTime(restritoInicioComBuffer);
            LocalDateTime novoPeriodoEnd = LocalDate.now().atTime(horaFim);

            // Busca agendamentos ativos na próxima ocorrência do diaSemana (para validar o futuro)
            validarContraAgendamentosAtivos(personalId, diaSemana, novoPeriodoStart, novoPeriodoEnd);        }
    }

    /**
     * Valida se o 'novo período' (Restrição) sobrepõe qualquer agendamento ATIVO + INTERVALO PÓS-ATENDIMENTO.
     */
    private void validarContraAgendamentosAtivos(Long personalId, DiaSemana diaSemana, LocalDateTime novoPeriodoStart, LocalDateTime novoPeriodoEnd) {
        Personal personal = personalRepository.findById(personalId).orElseThrow(PersonalNaoExisteExcepetion::new);
        final int bufferPosAtendimento = Optional.ofNullable(personal.getBufferMinutos()).orElse(15);

        // Encontra a próxima ocorrência do dia da semana (para buscar agendamentos)
        LocalDate dataRef = LocalDate.now();
        while (dataRef.getDayOfWeek() != diaSemana.getDayOfWeek()) {
            dataRef = dataRef.plusDays(1);
        }

        LocalDateTime start = dataRef.atStartOfDay();
        LocalDateTime end = dataRef.atTime(23,59,59);


        List<HorarioAgendadoProjection> agendamentos = produtoExibicaoRepository
                .findAgendamentoSlotsByPersonalIdAndDataBetween(personalId, start, end);

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
}