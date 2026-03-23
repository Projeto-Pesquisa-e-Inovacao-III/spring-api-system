package com.spring.ApiSystem.domain.agendamento.dto.response;

import java.time.LocalDate;

public record ResAgendamentoDataAndNameDto (
        LocalDate data,
        String alunoName
){}
