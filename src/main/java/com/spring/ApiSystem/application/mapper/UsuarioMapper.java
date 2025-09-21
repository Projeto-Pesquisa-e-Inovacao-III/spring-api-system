package com.spring.ApiSystem.application.mapper;
import com.spring.ApiSystem.domain.entity.Usuario;
import com.spring.ApiSystem.interfaces.dto.usuario.CadastroUsuarioDTO;
import com.spring.ApiSystem.interfaces.dto.usuario.EditarUsuarioDTO;
import com.spring.ApiSystem.interfaces.dto.usuario.ResUsuarioDTO;

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
