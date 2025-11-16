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
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class DisponibilidadePersonalService {

    private final DisponibilidadePersonalRepository disponibilidadeRepository;
    private final PersonalRepository personalRepository;


    public DisponibilidadePersonalService(DisponibilidadePersonalRepository disponibilidadeRepository, PersonalRepository personalRepository) {
        this.disponibilidadeRepository = disponibilidadeRepository;
        this.personalRepository = personalRepository;
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


    //Listagem dos horarios por personal
    @Transactional(readOnly = true)
    public List<ResHorarioDTO> listarHorariosPorPersonal(Long personalId) {

        if (!personalRepository.existsById(personalId)) {
            throw new PersonalNaoExisteExcpetion();
        }

        return disponibilidadeRepository.findByPersonalId(personalId).stream()
                .map(ResHorarioDTO::new)
                .collect(Collectors.toList());
    }


    // Consulta as disponibilidades dos horarios de um personal.
    @Transactional(readOnly = true)
    public List<ResSlotDisponivelDTO> obterHorariosDisponiveis(Long personalId, LocalDate dataDesejada) {


        // Garante que o Personal existe antes de processar
        if (!personalRepository.existsById(personalId)) {
            throw new PersonalNaoExisteExcpetion();
        }

        DayOfWeek diaSemana = dataDesejada.getDayOfWeek();
        List<DisponibilidadePersonal> disponibilidade = disponibilidadeRepository
                .findByPersonalIdAndDiaSemana(personalId, DiaSemana.fromDayOfWeek(diaSemana));

        if (disponibilidade.isEmpty()){
            return Collections.emptyList();
        }

        Set<LocalTime> horariosBloqueados = new HashSet<>();
        List<DisponibilidadePersonal> blocosDisponibilidade = new ArrayList<>();

        for(DisponibilidadePersonal disp : disponibilidade){
            if(disp.getTipo() == TipoHorario.DISPONIVEL){
                blocosDisponibilidade.add(disp);
            }else {
                LocalTime inicio = disp.getHoraInicio();
                LocalTime fim = disp.getHoraFim();

                LocalTime current = inicio;

                while (current.isBefore(fim)) {
                    horariosBloqueados.add(current);
                    current = current.plusMinutes(15);
                }
            }
        }

        List<LocalTime> slotsFinais = new ArrayList<>();

        for(DisponibilidadePersonal bloco : blocosDisponibilidade){
            LocalTime inicio = bloco.getHoraInicio();
            LocalTime fim = bloco.getHoraFim();
            LocalTime current = inicio;

            while(current.isBefore(fim)) {

                if(!horariosBloqueados.contains(current)){
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

                    if (tipo == TipoHorario.RESTRITO && existenteTipo == TipoHorario.RESTRITO) {
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
            throw new SobreposicaoHorarioException();
        }
    }




}