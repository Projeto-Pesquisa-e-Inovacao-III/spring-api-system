package com.spring.ApiSystem.domain.beneficio.mapper;

import com.spring.ApiSystem.domain.beneficio.Beneficio;
import com.spring.ApiSystem.domain.beneficio.dto.request.ReqCreateBeneficioDTO;
import com.spring.ApiSystem.domain.beneficio.dto.response.ResListBeneficioDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface BeneficioMapper {
    ResListBeneficioDTO toResDto(Beneficio beneficio);
    List<ResListBeneficioDTO> toResDtoList(List<Beneficio> beneficios);

    @Mapping(target = "id", ignore = true)
    Beneficio toEntity(ReqCreateBeneficioDTO dto);

    @Mapping(target = "id", ignore = true)
    List<Beneficio> toEntityList(List<ReqCreateBeneficioDTO> reqBeneficioDTOList);
}
