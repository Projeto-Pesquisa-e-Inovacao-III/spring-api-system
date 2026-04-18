package com.spring.ApiSystem.domain.admin.dto.request;

import com.spring.ApiSystem.domain.usuario.enums.Role;

public record ReqRemoverRoleDTO(
        Role role
) { }
