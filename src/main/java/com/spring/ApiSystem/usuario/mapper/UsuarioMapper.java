package com.spring.ApiSystem.usuario.mapper;
import com.spring.ApiSystem.usuario.dto.request.LoginUsuarioDTO;
import com.spring.ApiSystem.aluno.Aluno;
import com.spring.ApiSystem.usuario.Usuario;
import com.spring.ApiSystem.usuario.dto.request.CadastroUsuarioDTO;
import com.spring.ApiSystem.usuario.dto.request.EditarUsuarioDTO;
import com.spring.ApiSystem.usuario.dto.response.ResUsuarioDTO;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface UsuarioMapper {

    ResUsuarioDTO toDto(Usuario usuario);

    @Mapping(target = "id", ignore = true)
    Usuario toEntity(CadastroUsuarioDTO usuarioDTO);

    Usuario toEntity(ResUsuarioDTO usuarioDTO);

    @Mapping(target = "id", ignore = true)
    Usuario toEntity(EditarUsuarioDTO usuarioDTO);

    @Mapping(target = "id", ignore = true)
    Usuario toEntity(LoginUsuarioDTO usuarioDTO);

    Aluno toEntityAluno(CadastroUsuarioDTO usuarioDTO);

    @Mapping(target = "senha", ignore = true)
    void atualizarUsuarioFromEditarUsuarioDto(EditarUsuarioDTO dto,
                                  @MappingTarget Usuario usuario);

}
