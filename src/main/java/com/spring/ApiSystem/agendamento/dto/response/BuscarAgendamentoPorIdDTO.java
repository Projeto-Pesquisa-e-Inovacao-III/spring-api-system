package com.spring.ApiSystem.agendamento.dto.response;

import com.spring.ApiSystem.agendamento.dto.response.buscarporid.AlunoResumoDTO;
import com.spring.ApiSystem.agendamento.dto.response.buscarporid.EnderecoResumoDTO;
import com.spring.ApiSystem.agendamento.dto.response.buscarporid.PersonalResumoDTO;
import com.spring.ApiSystem.agendamento.enums.Situacao;
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

