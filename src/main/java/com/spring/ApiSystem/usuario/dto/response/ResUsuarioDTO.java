package com.spring.ApiSystem.usuario.dto.response;

import java.util.Date;

public record ResUsuarioDTO(
    Long id,
    String nome,
    String sexo,
    Date dataNascimento,
    String email,
    boolean ativo
) {}
