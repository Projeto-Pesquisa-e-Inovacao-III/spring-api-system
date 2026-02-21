package com.spring.ApiSystem.domain.agendamento.dto.request;

public record ReqCancelarAgendamento(
        Long idAgendamento,
        String descricaoCancelamento)
{}
