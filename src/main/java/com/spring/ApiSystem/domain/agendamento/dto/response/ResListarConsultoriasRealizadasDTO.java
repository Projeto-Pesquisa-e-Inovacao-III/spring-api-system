package com.spring.ApiSystem.domain.agendamento.dto.response;

public record ResListarConsultoriasRealizadasDTO(
        Integer mes,
        Integer ano,
        Integer totalConsultorias
) {}
