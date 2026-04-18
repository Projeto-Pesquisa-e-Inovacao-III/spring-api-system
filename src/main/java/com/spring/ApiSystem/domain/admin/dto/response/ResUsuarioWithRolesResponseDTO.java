package com.spring.ApiSystem.domain.admin.dto.response;

import java.util.List;

public record ResUsuarioWithRolesResponseDTO(
    Long id,
    String nome,
    String email,
    List<String> roles,
    String cpf,
    String anamnese,
    String cref
) {}
