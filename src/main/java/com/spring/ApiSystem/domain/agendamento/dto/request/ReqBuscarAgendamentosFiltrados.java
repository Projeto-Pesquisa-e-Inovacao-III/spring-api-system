package com.spring.ApiSystem.domain.agendamento.dto.request;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.spring.ApiSystem.domain.agendamento.enums.AgendamentoStatus;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

public record ReqBuscarAgendamentosFiltrados(

        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
        LocalDateTime dataInicio,

        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
        LocalDateTime dataFim,

        AgendamentoStatus status,
        String  nome
) {}
