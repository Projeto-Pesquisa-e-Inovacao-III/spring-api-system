package com.spring.ApiSystem.mapper;

import com.spring.ApiSystem.dto.endereco.request.EdicaoEnderecoDTO;
import com.spring.ApiSystem.dto.endereco.request.EnderecoDTO;
import com.spring.ApiSystem.model.Endereco;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface EnderecoMapper {

    void atualizarEnderecoFromDto(EnderecoDTO dto,
                                  @MappingTarget Endereco endereco);
    void atualizarEnderecoDtoFromDto(EdicaoEnderecoDTO dto,
                                     @MappingTarget EnderecoDTO endereco);

    Endereco toEntity(EnderecoDTO enderecoDTO);
}
