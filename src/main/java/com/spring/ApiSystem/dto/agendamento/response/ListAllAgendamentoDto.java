package com.spring.ApiSystem.dto.agendamento.response;

import com.spring.ApiSystem.dto.ProdutoContratado.response.ProdutoContratadoDto;
import com.spring.ApiSystem.dto.endereco.response.ResEnderecoDTO;
import com.spring.ApiSystem.dto.usuario.response.AlunoDto;
import com.spring.ApiSystem.dto.usuario.response.PersonalDto;
import com.spring.ApiSystem.model.enums.Situacao;
import jakarta.validation.constraints.NotNull;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * DTO for {@link com.spring.ApiSystem.model.Agendamento}
 */
public record ListAllAgendamentoDto(LocalDateTime data, Situacao situacao, String descricao,
                                    @NotNull ResEnderecoDTO endereco, @NotNull AlunoDto aluno,
                                    @NotNull PersonalDto personal,
                                    @NotNull ProdutoContratadoDto produtoContratado) implements Serializable {
}