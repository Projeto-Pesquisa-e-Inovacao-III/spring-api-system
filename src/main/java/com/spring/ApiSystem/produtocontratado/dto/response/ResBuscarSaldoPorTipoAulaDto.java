package com.spring.ApiSystem.produtocontratado.dto.response;

import com.spring.ApiSystem.produtoexibicao.enums.TipoAula;

public record ResBuscarSaldoPorTipoAulaDto(
        TipoAula tipoAula,
        Integer saldoAula
){}
