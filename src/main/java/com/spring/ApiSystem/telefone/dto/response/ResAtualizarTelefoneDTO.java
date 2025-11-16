package com.spring.ApiSystem.telefone.dto.response;

public record ResAtualizarTelefoneDTO(
        String ddd,
        String numero,
        String tipo,
        String numeroCompleto
) {
    public ResAtualizarTelefoneDTO(String ddd, String numero, String tipo) {
        this( ddd, numero, tipo, String.format("(%s) %s", ddd, numero));
    }
}
