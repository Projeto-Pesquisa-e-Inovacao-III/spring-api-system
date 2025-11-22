package com.spring.ApiSystem.usuario.dto.request;

import com.fasterxml.jackson.annotation.JsonInclude;

public record ReqAuthDTO(
        Boolean autentificado,
        @JsonInclude(JsonInclude.Include.NON_NULL)
        ReqAuthUserDTO user
) { }


