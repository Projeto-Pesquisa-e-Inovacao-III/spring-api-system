package com.spring.ApiSystem.domain.admin.dto.response;

import java.util.List;

public record ResUsuarioWithRolesResponseDTO(
    Long id,
    String nome,
    String email,
    boolean ativo,
    List<String> roles,
    String cpf,
    String anamnese,
    String cref
) {}
