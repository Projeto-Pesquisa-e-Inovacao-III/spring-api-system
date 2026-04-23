package com.spring.ApiSystem.domain.usuario.dto.request;

import com.spring.ApiSystem.domain.usuario.enums.Role;

import java.util.Set;

public record ReqAuthUserDTO(
        Long id,
        String nome,
        Set<Role> roles
){}
