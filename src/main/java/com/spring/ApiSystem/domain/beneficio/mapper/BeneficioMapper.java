package com.spring.ApiSystem.domain.beneficio.mapper;

import com.spring.ApiSystem.domain.beneficio.Beneficio;
import com.spring.ApiSystem.domain.beneficio.dto.request.ReqBeneficioDTO;
import com.spring.ApiSystem.domain.beneficio.dto.response.ResBeneficioDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface BeneficioMapper {
    ResBeneficioDTO toResDto(Beneficio beneficio);
    List<ResBeneficioDTO> toResDtoList(List<Beneficio> beneficios);

    @Mapping(target = "id", ignore = true)
    Beneficio toEntity(ReqBeneficioDTO dto);

    @Mapping(target = "id", ignore = true)
    List<Beneficio> toEntityList(List<ReqBeneficioDTO> reqBeneficioDTOList);
}
