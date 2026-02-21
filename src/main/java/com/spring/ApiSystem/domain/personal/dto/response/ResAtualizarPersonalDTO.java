package com.spring.ApiSystem.domain.personal.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.spring.ApiSystem.domain.telefone.dto.response.ResListarTelefonesPorIdDoUsuario;


import java.time.LocalDate;
import java.util.List;

public record ResAtualizarPersonalDTO(
        Long id,
        String nome,
        String sexo,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
        LocalDate dataNascimento,
        String email,
        String cref,
        boolean ativo,
        String caminhoFoto,
        List<ResListarTelefonesPorIdDoUsuario> telefones
) {
}
