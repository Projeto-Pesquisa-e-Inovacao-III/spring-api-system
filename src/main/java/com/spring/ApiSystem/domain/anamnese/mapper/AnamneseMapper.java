package com.spring.ApiSystem.domain.anamnese.mapper;

import com.spring.ApiSystem.domain.anamnese.Anamnese;
import com.spring.ApiSystem.domain.anamnese.dto.request.ReqAtualizarAnamneseDto;
import com.spring.ApiSystem.domain.anamnese.dto.request.ReqCadastrarAnamneseDto;
import com.spring.ApiSystem.domain.anamnese.dto.response.ResBuscarAnamneseDto;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface AnamneseMapper {

    Anamnese toEntityFromRequest(ReqCadastrarAnamneseDto dto);

    ResBuscarAnamneseDto toEntityFromRequest(Anamnese dto);

    void updateEntityFromRequest(ReqAtualizarAnamneseDto dto, @MappingTarget Anamnese anamnese);
}
