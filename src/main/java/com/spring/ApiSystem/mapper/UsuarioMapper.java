package com.spring.ApiSystem.mapper;
import com.spring.ApiSystem.entity.Usuario;
import com.spring.ApiSystem.dto.usuario.request.CadastroUsuarioDTO;
import com.spring.ApiSystem.dto.usuario.request.EditarUsuarioDTO;
import com.spring.ApiSystem.dto.usuario.response.ResUsuarioDTO;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UsuarioMapper {

    ResUsuarioDTO toDto(Usuario usuario);

    @Mapping(target = "id", ignore = true)
    Usuario toEntity(CadastroUsuarioDTO usuarioDTO);

    Usuario toEntity(ResUsuarioDTO usuarioDTO);

    @Mapping(target = "id", ignore = true)
    Usuario toEntity(EditarUsuarioDTO usuarioDTO);

}
