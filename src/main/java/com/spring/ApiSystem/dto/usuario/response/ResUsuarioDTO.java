package com.spring.ApiSystem.dto.usuario.response;

import java.util.Date;

public record ResUsuarioDTO(
    Long id,
    String nome,
    String sexo,
    Date dataNascimento,
    String email,
    boolean ativo
) {}
