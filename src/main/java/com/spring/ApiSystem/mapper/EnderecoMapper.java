package com.spring.ApiSystem.mapper;

import com.spring.ApiSystem.dto.agendamento.response.buscarporid.EnderecoResumoDTO;
import com.spring.ApiSystem.dto.endereco.request.EnderecoDTO;
import com.spring.ApiSystem.dto.endereco.response.ResEnderecoDTO;
import com.spring.ApiSystem.model.Endereco;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface EnderecoMapper {

    @Mapping(target = "cep", ignore = true)
    void atualizarEnderecoFromDto(EnderecoDTO dto,
                                  @MappingTarget Endereco endereco);

    @Mapping(target = "cep", ignore = true)
    Endereco toEntity(EnderecoDTO enderecoDTO);

    ResEnderecoDTO toResEnderecoDTO(Endereco endereco);

    Endereco toEntity(ResEnderecoDTO resEnderecoDTO);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Endereco partialUpdate(ResEnderecoDTO resEnderecoDTO, @MappingTarget Endereco endereco);


    EnderecoResumoDTO toEnderecoResumoDTO(Endereco endereco);
}
