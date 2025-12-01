package com.spring.ApiSystem.horariopersonal.dto.response;

import com.spring.ApiSystem.enums.DiaSemana;
import com.spring.ApiSystem.enums.TipoHorario;
import com.spring.ApiSystem.horariopersonal.DisponibilidadePersonal;

import java.time.LocalTime;


public record ResHorarioDTO(
        Long id,
        Long personalId,
        DiaSemana diaSemana,
        TipoHorario tipo,
        LocalTime horaInicio,
        LocalTime horaFim
) {

    public ResHorarioDTO(DisponibilidadePersonal horario) {
        this(
                horario.getId(),
                horario.getPersonal().getId(),
                horario.getDiaSemana(),
                horario.getTipo(),
                horario.getHoraInicio(),
                horario.getHoraFim()
        );
    }
}



