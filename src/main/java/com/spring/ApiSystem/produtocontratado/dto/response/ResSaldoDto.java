package com.spring.ApiSystem.produtocontratado.dto.response;

import com.spring.ApiSystem.produtoexibicao.enums.TipoAula;

public record ResSaldoDto(
        TipoAula tipoAula,
        Integer saldoAula
){}
