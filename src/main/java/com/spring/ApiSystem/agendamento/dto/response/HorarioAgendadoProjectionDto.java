package com.spring.ApiSystem.agendamento.dto.response;

import com.spring.ApiSystem.agendamento.enums.Situacao;

import java.time.LocalDateTime;


public record HorarioAgendadoProjectionDto(LocalDateTime dataInicio, String tipoAula, Situacao situacao) {
}
