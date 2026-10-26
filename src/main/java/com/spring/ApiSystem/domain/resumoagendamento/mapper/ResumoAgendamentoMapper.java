package com.spring.ApiSystem.domain.resumoagendamento.mapper;

import com.spring.ApiSystem.domain.resumoagendamento.ResumoAgendamento;
import com.spring.ApiSystem.domain.resumoagendamento.dto.res.ResCadastrarResumoAgendamentoDTO;
import com.spring.ApiSystem.domain.resumoagendamento.dto.res.ResResumoAgendamentoAlunoDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public abstract class ResumoAgendamentoMapper {

    @Mapping(target = "idAluno", source = "aluno.id")
    @Mapping(target = "idPersonal", source = "personal.id")
    public abstract ResCadastrarResumoAgendamentoDTO toResCadastrarResumoDTO(ResumoAgendamento resumoAgendamento);

    @Mapping(target = "nomeAluno", source = "aluno.nome")
    @Mapping(target = "nomePersonal", source = "personal.nome")
    @Mapping(target = "agendamentoData", source = "agendamento.data")
    public abstract ResResumoAgendamentoAlunoDTO toResResumoAgendamentoAlunoDTO(ResumoAgendamento resumoAgendamento);
    public abstract List<ResResumoAgendamentoAlunoDTO> toResResumoAgendamentoAlunoDTO(List<ResumoAgendamento> resumoAgendamento);
}
