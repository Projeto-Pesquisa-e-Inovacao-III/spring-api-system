package com.spring.ApiSystem.domain.disponibilidade.dto.response;

import com.spring.ApiSystem.shared.enums.DiaSemana;

public record ResDiaSemanaAndAtivoDTO (
        DiaSemana diaSemana,
        boolean ativo
){}
