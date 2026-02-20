package com.spring.ApiSystem.agendamento.dto.request;

import com.spring.ApiSystem.usuario.enums.TipoUsuario;

public record ReqRegistrarAusenciaAgendamento(
        Long idAgendamento,
        String descricaoCancelamento,
        TipoUsuario tipoUsuario
)
{}
