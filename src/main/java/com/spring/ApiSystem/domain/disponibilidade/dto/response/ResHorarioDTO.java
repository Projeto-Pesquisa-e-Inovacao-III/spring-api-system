package com.spring.ApiSystem.domain.disponibilidade.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.spring.ApiSystem.domain.disponibilidade.DisponibilidadePersonal;
import com.spring.ApiSystem.shared.enums.DiaSemana;
import com.spring.ApiSystem.domain.disponibilidade.enums.TipoHorario;

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



