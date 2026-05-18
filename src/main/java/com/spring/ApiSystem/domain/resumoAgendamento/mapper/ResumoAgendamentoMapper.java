package com.spring.ApiSystem.domain.resumoAgendamento.mapper;

import com.spring.ApiSystem.domain.resumoAgendamento.ResumoAgendamento;
import com.spring.ApiSystem.domain.resumoAgendamento.dto.req.ReqCadastrarResumoAgendamentoDTO;
import com.spring.ApiSystem.domain.resumoAgendamento.dto.res.ResCadastrarResumoAgendamentoDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public abstract class ResumoAgendamentoMapper {

    @Mapping(target = "aluno.id", source = "idAluno")
    @Mapping(target = "personal.id", source = "idPersonal")
    public abstract ResumoAgendamento toEntity(ReqCadastrarResumoAgendamentoDTO dto);

    @Mapping(target = "idAluno", source = "aluno.id")
    @Mapping(target = "idPersonal", source = "personal.id")
    public abstract ResCadastrarResumoAgendamentoDTO toResCadastrarResumoDTO(ResumoAgendamento resumoAgendamento);
}
