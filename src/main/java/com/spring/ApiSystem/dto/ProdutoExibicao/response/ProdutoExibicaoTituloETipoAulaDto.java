package com.spring.ApiSystem.dto.ProdutoExibicao.response;

import com.spring.ApiSystem.model.ProdutoExibicao;

import java.io.Serializable;

/**
 * DTO for {@link ProdutoExibicao}
 */
public record ProdutoExibicaoTituloETipoAulaDto(String titulo, String tipoAula) implements Serializable {
}