package com.spring.ApiSystem.usuario.dto.request;

import com.spring.ApiSystem.usuario.enums.TipoUsuario;

public record ReqAuthUserDTO(
        Long id,
        String nome,
        TipoUsuario tipo
){}
