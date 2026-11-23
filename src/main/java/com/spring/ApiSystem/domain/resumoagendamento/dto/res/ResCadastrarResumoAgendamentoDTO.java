package com.spring.ApiSystem.domain.resumoagendamento.dto.res;

import java.util.List;

public record ResCadastrarResumoAgendamentoDTO(
        Long idAluno,
        Long idPersonal,
        String resumo,
        List<String> grupoMuscular
) {}
