package com.spring.ApiSystem.endereco.mapper;

import com.spring.ApiSystem.agendamento.dto.response.buscarporid.EnderecoResumoDTO;
import com.spring.ApiSystem.cep.mapper.CepMapper;
import com.spring.ApiSystem.endereco.dto.request.EnderecoDTO;

import com.spring.ApiSystem.endereco.Endereco;
import com.spring.ApiSystem.endereco.dto.request.ReqCadastrarEnderecoDTO;
import com.spring.ApiSystem.endereco.dto.response.*;
import org.mapstruct.*;

@Mapper(componentModel = "spring", uses = {CepMapper.class})
public interface EnderecoMapper {

    Endereco toEntity(EnderecoDTO enderecoDTO);
    Endereco toEntity(ReqCadastrarEnderecoDTO enderecoDTO);
    Endereco toEntity(ResCadastrarEnderecoDTO resEnderecoDTO);
    Endereco toEntty(ResListarEnderecoDTO resListarEnderecoDTO);
    Endereco toEntity(ResEnderecoSemIdDto enderecoSemIdDto);
    Endereco toEntity(ResAtualizarEnderecoDTO resAtuailizarEnderecoDTO);

    ResCadastrarEnderecoDTO toResCadastrarEnderecoDTO(Endereco endereco);
    ResListarEnderecoDTO toResListarEnderecoDTO(Endereco endereco);
    ResEnderecoSemIdDto toEnderecoSemIdDto(Endereco endereco);
    ResAtualizarEnderecoDTO toResAtualizarEnderecoDTO(Endereco endereco);
    EnderecoResumoDTO toEnderecoResumoDTO(Endereco endereco);
    EnderecoDTO toDTO(Endereco endereco);


    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Endereco partialUpdate(ResEnderecoDTO resEnderecoDTO, @MappingTarget Endereco endereco);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Endereco partialUpdate(ResEnderecoSemIdDto enderecoSemIdDto, @MappingTarget Endereco endereco);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Endereco partialUpdate(EnderecoDTO enderecoSemIdDto, @MappingTarget Endereco endereco);
}
