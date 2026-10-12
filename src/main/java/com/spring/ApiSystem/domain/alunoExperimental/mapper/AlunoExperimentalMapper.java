package com.spring.ApiSystem.domain.alunoExperimental.mapper;


import com.spring.ApiSystem.domain.alunoExperimental.AlunoExperimental;
import com.spring.ApiSystem.domain.alunoExperimental.dto.ReqCriarAgendamentoExperimentalRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AlunoExperimentalMapper {

    @Mapping(target = "nome", source = "nomeAluno")
    @Mapping(target = "email", source = "emailAluno")
    AlunoExperimental toEntity(ReqCriarAgendamentoExperimentalRequest request);


}
