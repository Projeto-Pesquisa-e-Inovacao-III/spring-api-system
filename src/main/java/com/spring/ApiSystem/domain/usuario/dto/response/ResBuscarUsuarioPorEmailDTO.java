package com.spring.ApiSystem.domain.usuario.dto.response;

import java.time.LocalDate;

public record ResBuscarUsuarioPorEmailDTO(
        String nome,
        String sexo,
        LocalDate dataNascimento,
        String email,
        boolean ativo

) {
}
