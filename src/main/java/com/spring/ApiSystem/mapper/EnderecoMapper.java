package com.spring.ApiSystem.mapper;

import com.spring.ApiSystem.dto.endereco.request.EnderecoDTO;
import com.spring.ApiSystem.dto.endereco.response.ResEnderecoDTO;
import com.spring.ApiSystem.model.Endereco;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface EnderecoMapper {

    @Mapping(target = "cep", ignore = true)
    void atualizarEnderecoFromDto(EnderecoDTO dto,
                                  @MappingTarget Endereco endereco);

    @Mapping(target = "cep", ignore = true)
    Endereco toEntity(EnderecoDTO enderecoDTO);

    ResEnderecoDTO toResEnderecoDTO(Endereco endereco);
}
