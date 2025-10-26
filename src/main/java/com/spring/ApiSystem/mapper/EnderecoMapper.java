package com.spring.ApiSystem.mapper;

import com.spring.ApiSystem.dto.agendamento.response.buscarporid.EnderecoResumoDTO;
import com.spring.ApiSystem.dto.endereco.request.EnderecoDTO;
import com.spring.ApiSystem.dto.endereco.response.EnderecoSemIdDto;
import com.spring.ApiSystem.dto.endereco.response.ResEnderecoDTO;
import com.spring.ApiSystem.model.Endereco;
import org.mapstruct.*;

@Mapper(componentModel = "spring", uses = {CepMapper.class})
public interface EnderecoMapper {

    void atualizarEnderecoFromDto(EnderecoDTO dto, @MappingTarget Endereco endereco);

    Endereco toEntity(EnderecoDTO enderecoDTO);
    Endereco toEntity(ResEnderecoDTO resEnderecoDTO);
    Endereco toEntity(EnderecoSemIdDto enderecoSemIdDto);

    ResEnderecoDTO toResEnderecoDTO(Endereco endereco);
    EnderecoResumoDTO toEnderecoResumoDTO(Endereco endereco);
    EnderecoSemIdDto toEnderecoSemIdDto(Endereco endereco);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Endereco partialUpdate(ResEnderecoDTO resEnderecoDTO, @MappingTarget Endereco endereco);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Endereco partialUpdate(EnderecoSemIdDto enderecoSemIdDto, @MappingTarget Endereco endereco);
}
