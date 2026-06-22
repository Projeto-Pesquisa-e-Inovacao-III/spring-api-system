package com.spring.ApiSystem.domain.resumoagendamento.dto.res;

import com.spring.ApiSystem.domain.aluno.Aluno;
import com.spring.ApiSystem.domain.personal.Personal;

public record ResUsuarioResumoDTO(
        Long id,
        String nome
) {
    public static ResUsuarioResumoDTO from(Aluno aluno) {
        return new ResUsuarioResumoDTO(aluno.getId(), aluno.getNome());
    }

    public static ResUsuarioResumoDTO from(Personal personal) {
        return new ResUsuarioResumoDTO(personal.getId(), personal.getNome());
    }
}
