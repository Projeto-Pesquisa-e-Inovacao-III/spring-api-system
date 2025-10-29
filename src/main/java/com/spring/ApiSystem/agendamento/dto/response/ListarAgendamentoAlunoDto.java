package com.spring.ApiSystem.agendamento.dto.response;

import com.spring.ApiSystem.agendamento.Agendamento;
import com.spring.ApiSystem.personal.dto.response.PersonalNomeDto;
import com.spring.ApiSystem.agendamento.enums.Situacao;
import com.spring.ApiSystem.endereco.dto.response.EnderecoSemIdDto;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * DTO for {@link Agendamento}
 */
public record ListarAgendamentoAlunoDto(Long id, LocalDateTime data, Situacao situacao, String descricao,
                                        EnderecoSemIdDto endereco, PersonalNomeDto personal,
                                        AulaDto aula) implements Serializable {
}