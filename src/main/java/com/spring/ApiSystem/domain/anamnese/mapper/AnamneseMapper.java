package com.spring.ApiSystem.domain.anamnese.mapper;

import com.spring.ApiSystem.domain.anamnese.Anamnese;
import com.spring.ApiSystem.domain.anamnese.dto.request.ReqAtualizarAnamneseDto;
import com.spring.ApiSystem.domain.anamnese.dto.request.ReqCadastrarAnamneseDto;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface AnamneseMapper {

    Anamnese toEntityFromRequest(ReqCadastrarAnamneseDto dto);

    void updateEntityFromRequest(ReqAtualizarAnamneseDto dto, @MappingTarget Anamnese anamnese);
}
