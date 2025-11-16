package com.spring.ApiSystem.aluno.mapper;

import com.spring.ApiSystem.aluno.Aluno;
import com.spring.ApiSystem.aluno.dto.request.ReqAtualizarAlunoDTO;
import com.spring.ApiSystem.aluno.dto.request.ReqCadastroAlunoDTO;
import com.spring.ApiSystem.aluno.dto.response.ResAtualizarAlunoDTO;
import com.spring.ApiSystem.aluno.dto.response.ResBuscarAlunoPorIdDTO;
import com.spring.ApiSystem.aluno.dto.response.ResCadastrarAlunoDTO;
import com.spring.ApiSystem.telefone.Telefone;
import com.spring.ApiSystem.telefone.dto.response.ResListarTelefonesPorIdDoUsuario;
import com.spring.ApiSystem.telefone.mapper.TelefoneMapper;
import com.spring.ApiSystem.usuario.Usuario;
import com.spring.ApiSystem.aluno.dto.response.ResListarAlunosDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface AlunoMapper {

    @Mapping(target = "tipo", constant = "ALUNO")
    Aluno toEntityAluno(ReqCadastroAlunoDTO usuarioDTO);

    ResCadastrarAlunoDTO toDtoCadastrarAluno(Aluno aluno);

    ResBuscarAlunoPorIdDTO toDtoBuscarAlunoPorId(Aluno aluno);

    default ResListarTelefonesPorIdDoUsuario telefoneToDto(Telefone telefone) {
        if (telefone == null) return null;
        return new ResListarTelefonesPorIdDoUsuario(
                telefone.getId(),
                telefone.getDdd(),
                telefone.getNumero()
        );
    }

    ResAtualizarAlunoDTO toDtoAtualizarAluno(Aluno aluno);

    @Mapping(target = "id", ignore = true)
    Usuario toEntity(ReqAtualizarAlunoDTO alunoDTO);

    @Mapping(target = "senha", ignore = true)
    @Mapping(target = "telefones", ignore = true)
    void atualizarAlunoParaAtualizarAlunoDto(ReqAtualizarAlunoDTO dto,
                                              @MappingTarget Aluno aluno);

    List<ResListarAlunosDto> toResListarAlunosDto(List<Aluno> alunos);
}
