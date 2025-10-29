package com.spring.ApiSystem.agendamento.dto.response;

import com.spring.ApiSystem.agendamento.Agendamento;
import com.spring.ApiSystem.aluno.dto.response.AlunoNomeDto;
import com.spring.ApiSystem.agendamento.enums.Situacao;
import com.spring.ApiSystem.endereco.dto.response.EnderecoSemIdDto;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * DTO for {@link Agendamento}
 */
public record ListarAgendamentoPersonalDto(Long id, LocalDateTime data, Situacao situacao, String descricao,
                                           EnderecoSemIdDto endereco, AlunoNomeDto aluno,
                                           AulaDto aula) implements Serializable {
}