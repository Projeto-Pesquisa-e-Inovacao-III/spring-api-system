package com.spring.ApiSystem.dto.agendamento.response;

import com.spring.ApiSystem.dto.agendamento.response.buscarporid.AlunoResumoDTO;
import com.spring.ApiSystem.dto.agendamento.response.buscarporid.EnderecoResumoDTO;
import com.spring.ApiSystem.dto.agendamento.response.buscarporid.PersonalResumoDTO;
import com.spring.ApiSystem.dto.agendamento.response.buscarporid.ProdutoContratadoResumoDTO;
import com.spring.ApiSystem.model.enums.Situacao;
import java.time.LocalDateTime;

public record BuscarAgendamentoPorIdDTO(
    Long id,
    LocalDateTime data,
    String descricao,
    Situacao situacao,
    EnderecoResumoDTO endereco,
    AlunoResumoDTO aluno,
    PersonalResumoDTO personal,
    AulaDto aula
) {}

