package com.spring.ApiSystem.domain.horariopersonal.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.spring.ApiSystem.domain.horariopersonal.DisponibilidadePersonal;
import com.spring.ApiSystem.domain.horariopersonal.enums.DiaSemana;
import com.spring.ApiSystem.domain.horariopersonal.enums.TipoHorario;

import java.time.LocalTime;


public record ResHorarioDTO(
        Long id,
        Long personalId,
        DiaSemana diaSemana,
        TipoHorario tipo,

        @JsonFormat(pattern = "HH:mm")
        LocalTime horaInicio,
        @JsonFormat (pattern = "HH:mm")
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



