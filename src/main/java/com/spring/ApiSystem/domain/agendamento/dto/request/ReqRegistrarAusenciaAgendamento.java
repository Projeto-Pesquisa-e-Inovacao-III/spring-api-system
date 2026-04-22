package com.spring.ApiSystem.domain.agendamento.dto.request;

import com.spring.ApiSystem.domain.usuario.enums.Role;

public record ReqRegistrarAusenciaAgendamento(
        Long idAgendamento,
        String descricaoCancelamento,
        Role tipoUsuario
)
{}
