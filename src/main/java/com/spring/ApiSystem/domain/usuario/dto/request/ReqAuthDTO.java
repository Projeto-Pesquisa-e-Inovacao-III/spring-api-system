package com.spring.ApiSystem.domain.usuario.dto.request;

import com.fasterxml.jackson.annotation.JsonInclude;

public record ReqAuthDTO(
        Boolean autentificado,
        @JsonInclude(JsonInclude.Include.NON_NULL)
        ReqAuthUserDTO user,
        Boolean ativoAnamnese
) { }
