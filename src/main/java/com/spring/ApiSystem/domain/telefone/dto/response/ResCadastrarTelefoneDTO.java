package com.spring.ApiSystem.domain.telefone.dto.response;

public record ResCadastrarTelefoneDTO(
        Long id,
        String ddd,
        String numero,
        String numeroCompleto
) {
    public ResCadastrarTelefoneDTO(Long id, String ddd, String numero) {
        this(id, ddd, numero, String.format("(%s) %s", ddd, numero));
    }
}
