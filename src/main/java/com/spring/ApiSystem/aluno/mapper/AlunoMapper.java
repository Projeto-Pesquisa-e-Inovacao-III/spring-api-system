package com.spring.ApiSystem.aluno.mapper;

import com.spring.ApiSystem.aluno.Aluno;
import com.spring.ApiSystem.aluno.dto.request.ReqCadastroAlunoDTO;
import com.spring.ApiSystem.aluno.dto.response.ResCadastrarAlunoDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AlunoMapper {

    @Mapping(target = "tipo", constant = "ALUNO")
    Aluno toEntityAluno(ReqCadastroAlunoDTO usuarioDTO);
    ResCadastrarAlunoDTO toDtoCadastrarAluno(Aluno aluno);
}
