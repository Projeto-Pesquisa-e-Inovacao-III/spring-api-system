package com.spring.ApiSystem.domain.resumoagendamento.dto.res;

import com.spring.ApiSystem.domain.musculosecundario.MusculoSecundario;
import com.spring.ApiSystem.domain.resumoagendamento.ResumoAgendamento;

import java.util.List;

public record ResResumoDTO(
        Long id,
        String resumo,
        List<String> grupoMuscular,
        ResAgendamentoDTO agendamento,
        ResUsuarioResumoDTO aluno,
        ResUsuarioResumoDTO personal
) {
    public static ResResumoDTO from(ResumoAgendamento resumo) {
        return new ResResumoDTO(
                resumo.getId(),
                resumo.getResumo(),
                resumo.getGrupoMuscular().stream().map(MusculoSecundario::getName).toList(),
                ResAgendamentoDTO.from(resumo.getAgendamento()),
                ResUsuarioResumoDTO.from(resumo.getAluno()),
                ResUsuarioResumoDTO.from(resumo.getPersonal())
        );
    }
}
