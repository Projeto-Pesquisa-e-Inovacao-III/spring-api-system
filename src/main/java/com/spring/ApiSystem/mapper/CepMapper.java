package com.spring.ApiSystem.mapper;

import com.spring.ApiSystem.dto.Cep.response.CEPDto;
import com.spring.ApiSystem.model.CEP;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring")
public interface CepMapper {
    CEP toEntity(CEPDto CEPDto);

    CEPDto toDto(CEP CEP);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    CEP partialUpdate(CEPDto CEPDto, @MappingTarget CEP CEP);
}
