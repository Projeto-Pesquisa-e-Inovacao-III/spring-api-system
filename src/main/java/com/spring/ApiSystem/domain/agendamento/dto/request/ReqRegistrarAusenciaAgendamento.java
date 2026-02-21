package com.spring.ApiSystem.domain.agendamento.dto.request;

import com.spring.ApiSystem.domain.usuario.enums.TipoUsuario;

public record ReqRegistrarAusenciaAgendamento(
        Long idAgendamento,
        String descricaoCancelamento,
        TipoUsuario tipoUsuario
)
{}
