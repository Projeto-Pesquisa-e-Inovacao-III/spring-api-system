package com.spring.ApiSystem.endereco.mapper;

import com.spring.ApiSystem.agendamento.dto.response.buscarporid.EnderecoResumoDTO;
import com.spring.ApiSystem.cep.mapper.CepMapper;
import com.spring.ApiSystem.endereco.dto.request.EnderecoDTO;

import com.spring.ApiSystem.endereco.Endereco;
import com.spring.ApiSystem.endereco.dto.response.EnderecoSemIdDto;
import com.spring.ApiSystem.endereco.dto.response.ResEnderecoDTO;
import org.mapstruct.*;

@Mapper(componentModel = "spring", uses = {CepMapper.class})
public interface EnderecoMapper {
    Endereco toEntity(EnderecoDTO enderecoDTO);
    Endereco toEntity(ResEnderecoDTO resEnderecoDTO);
    Endereco toEntity(EnderecoSemIdDto enderecoSemIdDto);

    EnderecoDTO toDTO(Endereco endereco);
    ResEnderecoDTO toResEnderecoDTO(Endereco endereco);
    EnderecoResumoDTO toEnderecoResumoDTO(Endereco endereco);
    EnderecoSemIdDto toEnderecoSemIdDto(Endereco endereco);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Endereco partialUpdate(ResEnderecoDTO resEnderecoDTO, @MappingTarget Endereco endereco);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Endereco partialUpdate(EnderecoSemIdDto enderecoSemIdDto, @MappingTarget Endereco endereco);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Endereco partialUpdate(EnderecoDTO enderecoSemIdDto, @MappingTarget Endereco endereco);
}
