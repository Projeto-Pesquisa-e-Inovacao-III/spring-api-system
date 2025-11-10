package com.spring.ApiSystem.horariopersonal;


import com.spring.ApiSystem.enums.DiaSemana;
import com.spring.ApiSystem.enums.TipoHorario;
import com.spring.ApiSystem.horariopersonal.dto.request.ReqHorarioDTO;
import com.spring.ApiSystem.horariopersonal.dto.response.ResDiaDisponibilidadeDTO;
import com.spring.ApiSystem.horariopersonal.dto.response.ResHorarioDTO;
import com.spring.ApiSystem.horariopersonal.dto.response.ResSlotDisponivelDTO;
import com.spring.ApiSystem.horariopersonal.exception.SobreposicaoHorarioException;
import com.spring.ApiSystem.personal.Personal;
import com.spring.ApiSystem.personal.PersonalRepository;
import com.spring.ApiSystem.personal.exception.PersonalNaoExisteExcpetion;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class DisponibilidadePersonalService {

    private final DisponibilidadePersonalRepository disponibilidadeRepository;
    private final PersonalRepository personalRepository;


    public DisponibilidadePersonalService(DisponibilidadePersonalRepository disponibilidadeRepository, PersonalRepository personalRepository) {
        this.disponibilidadeRepository = disponibilidadeRepository;
        this.personalRepository = personalRepository;
    }


    //Consulta das disponibilidades de horario do fabião
    @Transactional(readOnly = true)
    public List<ResDiaDisponibilidadeDTO> buscarHorariosDisponiveis (Long personalId, Optional<DayOfWeek> diaSemanaFiltro) {
        List<DiaSemana> diasParaProcessar;
        if (diaSemanaFiltro.isPresent()) {
            diasParaProcessar = List.of(DiaSemana.fromDayOfWeek(diaSemanaFiltro.get()));

        }else {
            diasParaProcessar = List.of(DiaSemana.values());
        }

        List<ResDiaDisponibilidadeDTO> disponibilidadeTotal = new ArrayList<>();

        for (DiaSemana diaSemana : diasParaProcessar) {
            List<DisponibilidadePersonal> horariosDoDia = disponibilidadeRepository.findByPersonalIdAndDiaSemana(personalId, diaSemana);

            List<ResSlotDisponivelDTO> slotsLivres = calcularSlotsDisponiveis(horariosDoDia);

            if (!slotsLivres.isEmpty()) {
                disponibilidadeTotal.add(new ResDiaDisponibilidadeDTO(diaSemana, slotsLivres));

            }
        }

        disponibilidadeTotal.sort(Comparator.comparing(dia -> dia.dia().getDayOfWeek().getValue()));
        return disponibilidadeTotal;
    }


    // Crud principal para criar os horarios e a validação de sobreposição
    @Transactional
    public ResHorarioDTO criarHorario(Long personalId, ReqHorarioDTO request) {

        Personal personal = personalRepository.findById(personalId)
                .orElseThrow(PersonalNaoExisteExcpetion::new);

        validarConflito(personalId, request.diaSemana(), request.horaInicio(), request.horaFim(), null, request.tipo());

        DisponibilidadePersonal novoHorario = new DisponibilidadePersonal();
        novoHorario.setPersonal(personal);
        novoHorario.setDiaSemana(request.diaSemana());
        novoHorario.setTipo(request.tipo());
        novoHorario.setHoraInicio(request.horaInicio());
        novoHorario.setHoraFim(request.horaFim());

        DisponibilidadePersonal horarioSalvo = disponibilidadeRepository.save(novoHorario);
        return new ResHorarioDTO(horarioSalvo);

    }


    //Crud para atualizar um horário existente
    @Transactional
    public ResHorarioDTO atualizarHorario(Long horarioId, ReqHorarioDTO request) {

        DisponibilidadePersonal horarioExistente = disponibilidadeRepository.findById(horarioId)
                .orElseThrow(() -> new EntityNotFoundException("Horário não encontrado com ID: " + horarioId));

        Long personalId = horarioExistente.getPersonal().getId();

        validarConflito(personalId, request.diaSemana(), request.horaInicio(), request.horaFim(), horarioId, request.tipo());

        horarioExistente.setDiaSemana(request.diaSemana());
        horarioExistente.setTipo(request.tipo());
        horarioExistente.setHoraInicio(request.horaInicio());
        horarioExistente.setHoraFim(request.horaFim());

        DisponibilidadePersonal horarioSalvo = disponibilidadeRepository.save(horarioExistente);
        return new ResHorarioDTO(horarioSalvo);
    }


    //Deleta um horario pelo ID
    @Transactional
    public void deletarHorario(Long horarioId) {
        if (!disponibilidadeRepository.existsById(horarioId)) {
            throw new EntityNotFoundException("Horário não encontrado com ID: " + horarioId);
        }
        disponibilidadeRepository.deleteById(horarioId);
    }


    //Listagem dos horarios disponiveis para um personal
    @Transactional(readOnly = true)
    public List<ResHorarioDTO> listarHorariosPorPersonal(Long personalId) {

        if (!personalRepository.existsById(personalId)) {
            throw new PersonalNaoExisteExcpetion();
        }

        return disponibilidadeRepository.findByPersonalId(personalId).stream()
                .map(ResHorarioDTO::new)
                .collect(Collectors.toList());
    }


    // Consulta as disponibilidades dos horarios que estão disponiveis, passando um filtro por dia
    @Transactional(readOnly = true)
    public List<ResDiaDisponibilidadeDTO> obterHorariosDisponiveis(Long personalId, DiaSemana diaSemana) {
        List<ResDiaDisponibilidadeDTO> disponibilidadeTotal = new ArrayList<>();

        // Garante que o Personal existe antes de processar
        if (!personalRepository.existsById(personalId)) {
            throw new PersonalNaoExisteExcpetion();
        }


        List<DiaSemana> diasParaCalcular = new ArrayList<>();
        if (diaSemana != null) {
            diasParaCalcular.add(diaSemana);
        } else {
            diasParaCalcular.addAll(List.of(DiaSemana.values()));
        }

        for (DiaSemana dia : diasParaCalcular) {
            List<DisponibilidadePersonal> horariosDoDia = disponibilidadeRepository.findByPersonalIdAndDiaSemana(personalId, dia);

            List<ResSlotDisponivelDTO> slotsLivres = calcularSlotsDisponiveis(horariosDoDia);

            if (!slotsLivres.isEmpty()) {
                disponibilidadeTotal.add(new ResDiaDisponibilidadeDTO(dia, slotsLivres));
            }
        }

        disponibilidadeTotal.sort(Comparator.comparing(dia -> dia.dia().getDayOfWeek().getValue()));
        return disponibilidadeTotal;
    }


    //Metodos auxiliares

    private void validarConflito(Long personalId, DiaSemana diaSemana, LocalTime horaInicio, LocalTime horaFim, Long horarioId, TipoHorario tipo) {

        List<DisponibilidadePersonal> sobrepostos = disponibilidadeRepository.encontrarConflitos(
                personalId, diaSemana, horaInicio, horaFim, horarioId
        );

        if (sobrepostos.isEmpty()) {
            return;
        }

        TipoHorario novoTipo = tipo;

        List<DisponibilidadePersonal> conflitosFinais = sobrepostos.stream()
                .filter(existente -> {
                    TipoHorario existenteTipo = existente.getTipo();

                    if (novoTipo == TipoHorario.DISPONIVEL) {
                        return true;
                    }

                    if (novoTipo == TipoHorario.INTERVALO || novoTipo == TipoHorario.RESTRITO) {
                        if (existenteTipo == TipoHorario.INTERVALO || existenteTipo == TipoHorario.RESTRITO) {
                            return true;
                        }
                    }

                    return false;
                })
                .toList();


        if (!conflitosFinais.isEmpty()) {
            DisponibilidadePersonal conflito = conflitosFinais.get(0);

            throw new SobreposicaoHorarioException(
                    String.format("O horário informado (%s %s %s-%s) se sobrepõe a um horário já cadastrado (%s %s-%s).",
                            diaSemana, tipo, horaInicio, horaFim,
                            conflito.getTipo(), conflito.getHoraInicio(), conflito.getHoraFim()
                    )
            );
        }
    }

    private List<ResSlotDisponivelDTO> calcularSlotsDisponiveis(List<DisponibilidadePersonal> horariosDoDia) {

        List<DisponibilidadePersonal> disponibilidade = horariosDoDia.stream()
                .filter(h -> h.getTipo() == TipoHorario.DISPONIVEL)
                .collect(Collectors.toList());


        List<ResSlotDisponivelDTO> slotsDisponiveis = consolidarSlots(disponibilidade);


        List<DisponibilidadePersonal> restricoes = horariosDoDia.stream()
                .filter(h -> h.getTipo() == TipoHorario.INTERVALO || h.getTipo() == TipoHorario.RESTRITO)
                .sorted(Comparator.comparing(DisponibilidadePersonal::getHoraInicio))
                .toList();


        List<ResSlotDisponivelDTO> slotsFinais = new ArrayList<>();

        for (ResSlotDisponivelDTO slot : slotsDisponiveis) {
            List<ResSlotDisponivelDTO> slotsProcessados = List.of(slot);

            for (DisponibilidadePersonal restricao : restricoes) {
                List<ResSlotDisponivelDTO> slotsTemporarios = new ArrayList<>();
                for (ResSlotDisponivelDTO s : slotsProcessados) {
                    slotsTemporarios.addAll(subtrairRestricao(s, restricao));
                }
                slotsProcessados = slotsTemporarios;
            }
            slotsFinais.addAll(slotsProcessados);
        }

        return slotsFinais;
    }



    // O metodo pega uma lista dos horarios que estão Dispo e
    // junta os que estão em ordem(08-10h e 10-12h passam a ser 08h-12h)

    private List<ResSlotDisponivelDTO> consolidarSlots(List<DisponibilidadePersonal> horarios) {
        if (horarios.isEmpty()) return new ArrayList<>();

        horarios.sort(Comparator.comparing(DisponibilidadePersonal::getHoraInicio));

        List<ResSlotDisponivelDTO> slotsConsolidados = new ArrayList<>();

        DisponibilidadePersonal anterior = horarios.get(0);
        LocalTime inicioAtual = anterior.getHoraInicio();
        LocalTime fimAtual = anterior.getHoraFim();

        for (int i = 1; i < horarios.size(); i++) {
            DisponibilidadePersonal atual = horarios.get(i);

            if (atual.getHoraInicio().equals(fimAtual)) {

                fimAtual = atual.getHoraFim();
            } else {

                slotsConsolidados.add(new ResSlotDisponivelDTO(inicioAtual, fimAtual));
                inicioAtual = atual.getHoraInicio();
                fimAtual = atual.getHoraFim();
            }
        }

        slotsConsolidados.add(new ResSlotDisponivelDTO(inicioAtual, fimAtual));

        return slotsConsolidados;
    }


    private List<ResSlotDisponivelDTO> subtrairRestricao(ResSlotDisponivelDTO slotDisponivel, DisponibilidadePersonal restricao) {
        List<ResSlotDisponivelDTO> resultado = new ArrayList<>();


        LocalTime slotInicio = LocalTime.parse(slotDisponivel.inicio());
        LocalTime slotFim = LocalTime.parse(slotDisponivel.fim());
        LocalTime restInicio = restricao.getHoraInicio();
        LocalTime restFim = restricao.getHoraFim();

        // Caso 1: Restrição não toca o slot (antes ou depois)
        if (restFim.isBefore(slotInicio) || restFim.equals(slotInicio) || restInicio.isAfter(slotFim) || restInicio.equals(slotFim)) {
            resultado.add(slotDisponivel);
            return resultado;
        }

        // Caso 2: Restrição cobre parte do início do slot
        if (restInicio.isAfter(slotInicio) && restFim.isBefore(slotFim)) {
            resultado.add(new ResSlotDisponivelDTO(slotInicio, restInicio));
            resultado.add(new ResSlotDisponivelDTO(restFim, slotFim));
            return resultado;
        }

        // Caso 3: Restrição cobre o início do slot
        if (restInicio.isBefore(slotInicio) || restInicio.equals(slotInicio) && restFim.isBefore(slotFim)) {
            resultado.add(new ResSlotDisponivelDTO(restFim, slotFim));
            return resultado;
        }

        // Caso 4: Restrição cobre o fim do slot
        if (restInicio.isAfter(slotInicio) && (restFim.isAfter(slotFim) || restFim.equals(slotFim))) {
            resultado.add(new ResSlotDisponivelDTO(slotInicio, restInicio));
            return resultado;
        }

        // Caso 5: Restrição cobre o slot inteiro
        if ((restInicio.isBefore(slotInicio) || restInicio.equals(slotInicio)) && (restFim.isAfter(slotFim) || restFim.equals(slotFim))) {
            return resultado;
        }

        // Se nada bateu, retorna o slot original
        resultado.add(slotDisponivel);
        return resultado;
    }



}