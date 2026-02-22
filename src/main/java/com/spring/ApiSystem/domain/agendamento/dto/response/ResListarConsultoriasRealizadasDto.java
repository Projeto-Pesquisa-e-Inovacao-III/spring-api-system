package com.spring.ApiSystem.domain.agendamento.dto.response;

public record ResListarConsultoriasRealizadasDto(
        Integer mes,
        Integer ano,
        Integer totalConsultorias
) {}
