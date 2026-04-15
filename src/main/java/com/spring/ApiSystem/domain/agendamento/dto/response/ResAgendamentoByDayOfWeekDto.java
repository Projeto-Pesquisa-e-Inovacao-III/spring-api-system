package com.spring.ApiSystem.domain.agendamento.dto.response;

import java.time.LocalDate;

public record ResAgendamentoByDayOfWeekDto(
        Long id,
        LocalDate data,
        String alunoName,
        String pathImage
){}
