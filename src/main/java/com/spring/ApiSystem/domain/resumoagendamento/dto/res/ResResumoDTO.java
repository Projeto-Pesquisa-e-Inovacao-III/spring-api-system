package com.spring.ApiSystem.domain.resumoagendamento.dto.res;

import com.spring.ApiSystem.domain.resumoagendamento.ResumoAgendamento;
import com.spring.ApiSystem.domain.resumoagendamento.enums.GrupoMuscular;

import java.util.List;

public record ResResumoDTO(
        Long id,
        String resumo,
        List<GrupoMuscular> grupoMuscular,
        ResAgendamentoDTO agendamento,
        ResUsuarioResumoDTO aluno,
        ResUsuarioResumoDTO personal
) {
    public static ResResumoDTO from(ResumoAgendamento resumo) {
        return new ResResumoDTO(
                resumo.getId(),
                resumo.getResumo(),
                resumo.getGrupoMuscular(),
                ResAgendamentoDTO.from(resumo.getAgendamento()),
                ResUsuarioResumoDTO.from(resumo.getAluno()),
                ResUsuarioResumoDTO.from(resumo.getPersonal())
        );
    }
}
