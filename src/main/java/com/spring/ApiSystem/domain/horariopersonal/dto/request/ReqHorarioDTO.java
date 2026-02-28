package com.spring.ApiSystem.domain.horariopersonal.dto.request;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.spring.ApiSystem.domain.horariopersonal.enums.DiaSemana;
import com.spring.ApiSystem.domain.horariopersonal.enums.TipoHorario;
import jakarta.validation.constraints.NotNull;

import java.time.LocalTime;

public record ReqHorarioDTO(

        @NotNull(message = "O dia da semana não pode ser nulo")
        DiaSemana diaSemana,

        @NotNull(message = "O tipo de horário não pode ser nulo")
        TipoHorario tipo,

        @NotNull(message = "A hora de início não pode ser nula")
        @JsonFormat (pattern = "HH:mm")
        LocalTime horaInicio,

        @NotNull(message = "A hora de fim não pode ser nula")
        @JsonFormat (pattern = "HH:mm")
        LocalTime horaFim
) {}
