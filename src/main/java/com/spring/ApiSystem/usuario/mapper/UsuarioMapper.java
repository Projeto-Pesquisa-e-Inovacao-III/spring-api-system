package com.spring.ApiSystem.usuario.mapper;
import com.spring.ApiSystem.usuario.dto.request.ReqLoginUsuarioDTO;
import com.spring.ApiSystem.aluno.Aluno;
import com.spring.ApiSystem.usuario.Usuario;
import com.spring.ApiSystem.usuario.dto.request.ReqCadastroUsuarioDTO;
import com.spring.ApiSystem.usuario.dto.request.ReqEditarUsuarioDTO;
import com.spring.ApiSystem.usuario.dto.response.ResAtualizarUsuarioDTO;
import com.spring.ApiSystem.usuario.dto.response.ResCadastrarUsuarioDTO;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface UsuarioMapper {

    ResCadastrarUsuarioDTO toDtoCadastrarUsuario(Usuario usuario);
    ResAtualizarUsuarioDTO toDtoAtualizarUsuario(Usuario usuario);

    @Mapping(target = "id", ignore = true)
    Usuario toEntity(ReqCadastroUsuarioDTO usuarioDTO);

    Usuario toEntity(ResCadastrarUsuarioDTO usuarioDTO);

    @Mapping(target = "id", ignore = true)
    Usuario toEntity(ReqEditarUsuarioDTO usuarioDTO);

    @Mapping(target = "id", ignore = true)
    Usuario toEntity(ReqLoginUsuarioDTO usuarioDTO);

    Aluno toEntityAluno(ReqCadastroUsuarioDTO usuarioDTO);

    @Mapping(target = "senha", ignore = true)
    void atualizarUsuarioParaEditarUsuarioDto(ReqEditarUsuarioDTO dto,
                                              @MappingTarget Usuario usuario);



}
