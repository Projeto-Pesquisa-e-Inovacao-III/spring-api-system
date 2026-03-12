package com.spring.ApiSystem.domain.anamnese.mapper;

import com.spring.ApiSystem.domain.anamnese.dto.request.ReqCadastrarAnamneseDto;
import com.spring.ApiSystem.domain.anamnese.dto.response.ResCadastrarAnamneseDto;
import org.mapstruct.Mapper;

import com.spring.ApiSystem.domain.anamnese.Anamnese;

@Mapper(componentModel = "spring")
public interface AnamneseMapper {

    Anamnese toEntityFromRequest(ReqCadastrarAnamneseDto dto);
    Anamnese toEntityFromResponse(ResCadastrarAnamneseDto dto);
}
