package com.spring.ApiSystem.domain.personal.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.spring.ApiSystem.domain.telefone.dto.response.ResListarTelefonesPorIdDoUsuario;
import com.spring.ApiSystem.domain.usuario.enums.Role;


import java.time.LocalDate;
import java.util.List;
import java.util.Set;

public record ResBuscarPersonalPorIdDTO(
        Long id,
        String nome,
        String sexo,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
        LocalDate dataNascimento,
        String email,
        String cref,
        boolean ativo,
        String caminhoFoto,
        Set<Role> roles,
        List<ResListarTelefonesPorIdDoUsuario> telefones
) {}

