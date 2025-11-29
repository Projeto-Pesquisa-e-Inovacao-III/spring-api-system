package com.spring.ApiSystem.personal.dto.response;

public record ResListarPersonaisDTO(
    Long id,
    String nome,
    String caminhoFoto
) { }
