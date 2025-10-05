package com.spring.ApiSystem.mapper;

import com.spring.ApiSystem.dto.usuario.request.EditarEnderecoDTO;
import com.spring.ApiSystem.dto.usuario.request.EditarUsuarioDTO;
import com.spring.ApiSystem.model.Endereco;
import com.spring.ApiSystem.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface EnderecoMapper {
    @Mapping(target = "id", ignore = true)
    void atualizarEnderecoFromDto(EditarEnderecoDTO dto, @MappingTarget Endereco endereco);
}
