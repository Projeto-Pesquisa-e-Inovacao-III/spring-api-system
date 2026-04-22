package com.spring.ApiSystem.domain.telefone.dto.response;

public record ResAtualizarTelefoneDTO(
        Long id,
        String ddd,
        String numero,
        String numeroCompleto
) {
    public ResAtualizarTelefoneDTO(Long id, String ddd, String numero) {
        this(id, ddd, numero, String.format("(%s) %s", ddd, numero));
    }
}
