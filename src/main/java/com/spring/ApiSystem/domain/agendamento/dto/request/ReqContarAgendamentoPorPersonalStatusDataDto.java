package com.spring.ApiSystem.agendamento.dto.request;

import com.spring.ApiSystem.agendamento.enums.AgendamentoStatus;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

public record ReqContarAgendamentoPorPersonalStatusDataDto(
        @NotNull(message = "Status é obrigatório")
        AgendamentoStatus status,
        LocalDate data
){}
