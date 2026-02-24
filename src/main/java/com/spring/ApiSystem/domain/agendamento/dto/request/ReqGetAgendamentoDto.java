package com.spring.ApiSystem.domain.agendamento.dto.request;

import com.spring.ApiSystem.domain.agendamento.enums.AgendamentoStatus;
import com.spring.ApiSystem.domain.produtoexibicao.enums.TipoAula;

import java.time.LocalDateTime;

public record ReqGetAgendamentoDto(
        String nomeDoAluno,
        String status,
        String tipoAgendamento,
        LocalDateTime dataInic,
        LocalDateTime dataFim
)
{
    public AgendamentoStatus getStatusEnum() {
        return status != null &&  !status.isBlank() ?
                AgendamentoStatus.valueOf(status.toUpperCase()) : null;
    }

    public TipoAula getTipoAgendamentoEnum() {
        return tipoAgendamento != null && !tipoAgendamento.isBlank()  ?
                TipoAula.valueOf(tipoAgendamento.toUpperCase()) : null;
    }
}
