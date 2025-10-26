package com.spring.ApiSystem.mapper;

import com.spring.ApiSystem.dto.endereco.request.EnderecoDTO;
import com.spring.ApiSystem.dto.endereco.response.EnderecoSemIdDto;
import com.spring.ApiSystem.dto.endereco.response.ResEnderecoDTO;
import com.spring.ApiSystem.model.Endereco;
import org.mapstruct.*;

@Mapper(componentModel = "spring", uses = {CepMapper.class})
public interface EnderecoMapper {

    void atualizarEnderecoFromDto(EnderecoDTO dto, @MappingTarget Endereco endereco);

    Endereco toEntity(EnderecoDTO enderecoDTO);

    ResEnderecoDTO toResEnderecoDTO(Endereco endereco);

    Endereco toEntity(ResEnderecoDTO resEnderecoDTO);

    EnderecoSemIdDto toEnderecoSemIdDto(Endereco endereco);

    Endereco toEntity(EnderecoSemIdDto enderecoSemIdDto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Endereco partialUpdate(EnderecoSemIdDto enderecoSemIdDto, @MappingTarget Endereco endereco);
}
