package com.spring.ApiSystem.aluno.mapper;

import com.spring.ApiSystem.aluno.Aluno;
import com.spring.ApiSystem.aluno.dto.request.ReqCadastroAlunoDTO;
import com.spring.ApiSystem.aluno.dto.response.ResBuscarAlunoPorIdDTO;
import com.spring.ApiSystem.aluno.dto.response.ResCadastrarAlunoDTO;
import com.spring.ApiSystem.telefone.Telefone;
import com.spring.ApiSystem.telefone.dto.response.ResListarTelefonesPorIdDoUsuario;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AlunoMapper {

    @Mapping(target = "tipo", constant = "ALUNO")
    Aluno toEntityAluno(ReqCadastroAlunoDTO usuarioDTO);
    ResCadastrarAlunoDTO toDtoCadastrarAluno(Aluno aluno);
    ResBuscarAlunoPorIdDTO toDtoBuscarAlunoPorId(Aluno aluno);
    default ResListarTelefonesPorIdDoUsuario telefoneToDto(Telefone telefone) {
        if (telefone == null) return null;
        return new ResListarTelefonesPorIdDoUsuario(
                telefone.getDdd(),
                telefone.getNumero(),
                "FIXO" // ou telefone.getTipo() se existir
        );
    }
}
