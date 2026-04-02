package com.spring.ApiSystem.domain.agendamento.dto.response;

import java.time.LocalDate;

public record ResAgendamentoDataAndNameDto (
        Long id,
        LocalDate data,
        String alunoName,
        String pathImage
){}
