package com.spring.ApiSystem.domain.usuario.mapper;
import com.spring.ApiSystem.domain.usuario.dto.request.ReqAuthUserDTO;
import com.spring.ApiSystem.domain.usuario.dto.request.ReqLoginUsuarioDTO;
import com.spring.ApiSystem.domain.usuario.Usuario;
import com.spring.ApiSystem.domain.usuario.dto.request.ReqEditarUsuarioDTO;
import com.spring.ApiSystem.domain.usuario.dto.response.ResAtualizarUsuarioDTO;
import com.spring.ApiSystem.domain.usuario.dto.response.ResCadastrarUsuarioDTO;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface UsuarioMapper {

    ResCadastrarUsuarioDTO toDtoCadastrarUsuario(Usuario usuario);
    ResAtualizarUsuarioDTO toDtoAtualizarUsuario(Usuario usuario);

    @Mapping(target = "id", ignore = true)
    Usuario toEntity(ReqEditarUsuarioDTO usuarioDTO);

    @Mapping(target = "id", ignore = true)
    Usuario toEntity(ReqLoginUsuarioDTO usuarioDTO);

    ReqAuthUserDTO toDtoAuthUser(Usuario usuario);

    @Mapping(target = "senha", ignore = true)
    void atualizarUsuarioParaEditarUsuarioDto(ReqEditarUsuarioDTO dto,
                                              @MappingTarget Usuario usuario);



}
