package com.spring.ApiSystem.domain.usuario.dto.request;

import com.spring.ApiSystem.domain.usuario.enums.TipoUsuario;

public record ReqAuthUserDTO(
        Long id,
        String nome,
        TipoUsuario tipo
){}
