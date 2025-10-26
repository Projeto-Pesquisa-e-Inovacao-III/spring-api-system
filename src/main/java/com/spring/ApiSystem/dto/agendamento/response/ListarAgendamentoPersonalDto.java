package com.spring.ApiSystem.dto.agendamento.response;

import com.spring.ApiSystem.dto.usuario.response.AlunoNomeDto;
import com.spring.ApiSystem.dto.ProdutoContratado.response.ProdutoContratadoSoComProdutoDto;
import com.spring.ApiSystem.dto.endereco.response.EnderecoSemIdDto;
import com.spring.ApiSystem.model.enums.Situacao;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * DTO for {@link com.spring.ApiSystem.model.Agendamento}
 */
public record ListarAgendamentoPersonalDto(Long id, LocalDateTime data, Situacao situacao, String descricao,
                                           EnderecoSemIdDto endereco, AlunoNomeDto aluno,
                                           AulaDto aula) implements Serializable {
}