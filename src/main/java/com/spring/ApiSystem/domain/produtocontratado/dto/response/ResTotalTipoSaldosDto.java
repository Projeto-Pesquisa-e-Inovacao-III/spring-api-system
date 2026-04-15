package com.spring.ApiSystem.domain.produtocontratado.dto.response;

public record ResTotalTipoSaldosDto(
        Integer saldoPresencial,
        Integer saldoResidencial,
        Integer saldoFuncional
) {
}
