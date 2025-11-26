package com.spring.ApiSystem.agendamento.dto.response;

public record ResListarConsultoriasRealizadasDto(
        Integer mes,
        Integer ano,
        Integer totalConsultorias
) {}
