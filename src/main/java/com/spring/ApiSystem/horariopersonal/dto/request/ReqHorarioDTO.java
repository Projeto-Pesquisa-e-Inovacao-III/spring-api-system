package com.spring.ApiSystem.horariopersonal.dto.request;

import com.spring.ApiSystem.enums.DiaSemana;
import com.spring.ApiSystem.enums.TipoHorario;
import jakarta.validation.constraints.NotNull;

import java.time.LocalTime;

public record ReqHorarioDTO(

        @NotNull(message = "O dia da semana não pode ser nulo")
        DiaSemana diaSemana,

        @NotNull(message = "O tipo de horário não pode ser nulo")
        TipoHorario tipo,

        @NotNull(message = "A hora de início não pode ser nula")
        LocalTime horaInicio,

        @NotNull(message = "A hora de fim não pode ser nula")
        LocalTime horaFim

) {

}
