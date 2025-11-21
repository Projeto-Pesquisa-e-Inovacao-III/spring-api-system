package com.spring.ApiSystem.cep.mapper;

import com.spring.ApiSystem.cep.dto.response.CEPDto;
import com.spring.ApiSystem.cep.dto.response.DadosCepDTO;
import com.spring.ApiSystem.cep.CEP;
import com.spring.ApiSystem.cep.dto.response.ResBuscarSolicitacoesPorPersonalCEPDto;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring")
public interface CepMapper {
    CEP toEntity(CEPDto CEPDto);
    CEP toEntity(DadosCepDTO dadosCepDTO);

    CEPDto toDto(CEP CEP);
    ResBuscarSolicitacoesPorPersonalCEPDto buscarSolicitacoesPersonalPorCEP(CEP cep);
    DadosCepDTO toDadosCepDTO(CEP CEP);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    CEP partialUpdate(CEPDto CEPDto, @MappingTarget CEP CEP);
}
