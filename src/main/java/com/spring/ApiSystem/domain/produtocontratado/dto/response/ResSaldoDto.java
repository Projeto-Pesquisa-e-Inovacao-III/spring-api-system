package com.spring.ApiSystem.domain.produtocontratado.dto.response;


import com.spring.ApiSystem.domain.produtoexibicao.enums.TipoAula;

public record ResSaldoDto(
        TipoAula tipoAula,
        Integer saldoAula
){}
