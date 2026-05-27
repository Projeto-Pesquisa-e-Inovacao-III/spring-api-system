package com.spring.ApiSystem.domain.endereco.mapper;

import com.spring.ApiSystem.domain.cep.mapper.CepMapper;


import com.spring.ApiSystem.domain.endereco.Endereco;
import com.spring.ApiSystem.domain.endereco.dto.request.ReqAtualizarEnderecoDTO;
import com.spring.ApiSystem.domain.endereco.dto.request.ReqCadastrarEnderecoDTO;
import com.spring.ApiSystem.domain.endereco.dto.response.*;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring", uses = {CepMapper.class})
public interface EnderecoMapper {


    Endereco toEntity(ReqCadastrarEnderecoDTO enderecoDTO);
    Endereco toEntity(ReqAtualizarEnderecoDTO reqAtualizarEnderecoDTO);

    Endereco toEntity(ResCadastrarEnderecoDTO resEnderecoDTO);
    Endereco toEntity(ResListarEnderecoDTO resListarEnderecoDTO);
    Endereco toEntity(ResEnderecoSemIdDto enderecoSemIdDto);
    Endereco toEntity(ResAtualizarEnderecoDTO resAtualizarEnderecoDTO);

    ResCadastrarEnderecoDTO toResCadastrarEnderecoDTO(Endereco endereco);
    ResAtualizarEnderecoDTO toResAtualizarEnderecoDTO(Endereco endereco);

    @Mapping(target = "cidade", source = "cep.localidade")
    @Mapping(target = "uf", source = "cep.uf")
    @Mapping(target = "bairro", source = "cep.bairro")
    ResEnderecoAgendamentoDTO toResEnderecoAgendamentoDTO(Endereco endereco);
    List<ResListarEnderecoDTO> toResListarEnderecosDTO(List<Endereco> enderecos);
    List<ResListarEnderecoPorDataDeCriacaoDTO> toResListarEnderecosPorDataDeCriacaoDTO(List<Endereco> enderecos);


    ResAgendementoDadosEnderecoAlunoDTO toResEnderecoBuscaAgendamentosAlunosPorId(Endereco endereco);
    ResAgendementoDadosEnderecoPersonalDTO toResEnderecoBuscaAgendamentosPersonalPorId(Endereco endereco);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Endereco partialUpdate(ResEnderecoDTO resEnderecoDTO, @MappingTarget Endereco endereco);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Endereco partialUpdate(ResEnderecoSemIdDto enderecoSemIdDto, @MappingTarget Endereco endereco);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "cep", ignore = true)
    Endereco partialUpdate(ReqAtualizarEnderecoDTO enderecoSemIdDto, @MappingTarget Endereco endereco);
}
