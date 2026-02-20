package com.spring.ApiSystem.telefone.dto.response;

public record ResListarTelefonesPorIdDoUsuario(
        Long id,
        String ddd,
        String numero,
        String numeroCompleto
) {
    public ResListarTelefonesPorIdDoUsuario(Long id, String ddd, String numero) {
        this(id, ddd, numero, String.format("(%s) %s", ddd, numero));
    }
}