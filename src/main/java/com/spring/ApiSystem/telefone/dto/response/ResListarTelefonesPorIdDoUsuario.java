package com.spring.ApiSystem.telefone.dto.response;

public record ResListarTelefonesPorIdDoUsuario(
        String ddd,
        String numero,
        String tipo,
        String numeroCompleto
) {
    public ResListarTelefonesPorIdDoUsuario( String ddd, String numero, String tipo) {
        this( ddd, numero, tipo, String.format("(%s) %s", ddd, numero));
    }
}