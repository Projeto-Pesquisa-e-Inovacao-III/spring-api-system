package com.spring.ApiSystem.domain.agendamento.dto.response;



import com.spring.ApiSystem.domain.agendamento.enums.AgendamentoStatus;
import com.spring.ApiSystem.domain.produtoexibicao.enums.TipoAula;

import java.time.LocalDateTime;


public record HorarioAgendadoProjectionDto
        (LocalDateTime dataInicio,
         TipoAula tipoAula,
         AgendamentoStatus status)
{

}
