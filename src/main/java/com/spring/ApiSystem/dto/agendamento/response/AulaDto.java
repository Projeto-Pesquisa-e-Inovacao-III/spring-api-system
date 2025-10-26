package com.spring.ApiSystem.dto.agendamento.response;

import java.io.Serializable;

/**
 * DTO for Aula information extracted from ProdutoExibicao
 */
public record AulaDto(String titulo, String tipoAula) implements Serializable {
}