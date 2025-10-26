package com.spring.ApiSystem.mapper;

import com.spring.ApiSystem.dto.cep.response.CEPDto;
import com.spring.ApiSystem.dto.cep.response.DadosCepDTO;
import com.spring.ApiSystem.model.CEP;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring")
public interface CepMapper {
    CEP toEntity(CEPDto CEPDto);
    CEP toEntity(DadosCepDTO dadosCepDTO);

    CEPDto toDto(CEP CEP);
    DadosCepDTO toDadosCepDTO(CEP CEP);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    CEP partialUpdate(CEPDto CEPDto, @MappingTarget CEP CEP);
}
