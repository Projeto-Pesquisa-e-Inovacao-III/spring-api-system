package com.spring.ApiSystem.dto.agendamento.response;

import com.spring.ApiSystem.dto.endereco.response.EnderecoSemIdDto;
import com.spring.ApiSystem.model.Agendamento;
import com.spring.ApiSystem.dto.usuario.response.PersonalNomeDto;
import com.spring.ApiSystem.model.enums.Situacao;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * DTO for {@link Agendamento}
 */
public record ListarAgendamentoAlunoDto(Long id, LocalDateTime data, Situacao situacao, String descricao,
                                        EnderecoSemIdDto endereco, PersonalNomeDto personal,
                                        AulaDto aula) implements Serializable {
}