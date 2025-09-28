package com.spring.ApiSystem.mapper;
import com.spring.ApiSystem.dto.usuario.request.LoginUsuarioDTO;
import com.spring.ApiSystem.model.User;
import com.spring.ApiSystem.dto.usuario.request.CadastroUsuarioDTO;
import com.spring.ApiSystem.dto.usuario.request.EditarUsuarioDTO;
import com.spring.ApiSystem.dto.usuario.response.ResUsuarioDTO;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UsuarioMapper {

    ResUsuarioDTO toDto(User user);

    @Mapping(target = "id", ignore = true)
    User toEntity(CadastroUsuarioDTO usuarioDTO);

    User toEntity(ResUsuarioDTO usuarioDTO);

    @Mapping(target = "id", ignore = true)
    User toEntity(EditarUsuarioDTO usuarioDTO);

    @Mapping(target = "id", ignore = true)
    User toEntity(LoginUsuarioDTO usuarioDTO);

}
