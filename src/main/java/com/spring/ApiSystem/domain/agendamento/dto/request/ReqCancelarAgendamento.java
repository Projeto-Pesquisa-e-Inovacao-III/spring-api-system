package com.spring.ApiSystem.agendamento.dto.request;

public record ReqCancelarAgendamento(
        Long idAgendamento,
        String descricaoCancelamento)
{}
