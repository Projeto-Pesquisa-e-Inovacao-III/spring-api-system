package com.spring.ApiSystem.agendamento.dto.response;

import com.spring.ApiSystem.agendamento.enums.AgendamentoStatus;
import com.spring.ApiSystem.produtoexibicao.enums.TipoAula;

import java.time.LocalDateTime;


public record HorarioAgendadoProjectionDto
        (LocalDateTime dataInicio,
         TipoAula tipoAula,
         AgendamentoStatus status)
{

}
