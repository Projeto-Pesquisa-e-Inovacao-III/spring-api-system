package com.spring.ApiSystem.domain.anamnese.mapper;

import org.mapstruct.Mapper;

import com.spring.ApiSystem.domain.anamnese.Anamnese;
import com.spring.ApiSystem.domain.anamnese.dto.request.ReqCadastroAnamneseDto;
import com.spring.ApiSystem.domain.anamnese.dto.response.ResCadastroAnamneseDto;

@Mapper(componentModel = "spring")
public interface AnamneseMapper {

    Anamnese toEntityFromRequest(ReqCadastroAnamneseDto dto);
    Anamnese toEntityFromResponse(Anamnese anamnese);
}
