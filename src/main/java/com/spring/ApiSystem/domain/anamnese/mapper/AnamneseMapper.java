package com.spring.ApiSystem.domain.anamnese.mapper;

import com.spring.ApiSystem.domain.anamnese.Anamnese;
import com.spring.ApiSystem.domain.anamnese.dto.request.ReqAtualizarAnamneseDTO;
import com.spring.ApiSystem.domain.anamnese.dto.request.ReqCadastrarAnamneseDTO;
import com.spring.ApiSystem.domain.anamnese.dto.response.ResBuscarAnamneseDTO;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface AnamneseMapper {

    Anamnese toEntityFromRequest(ReqCadastrarAnamneseDTO dto);

    ResBuscarAnamneseDTO buscarAnamnese(Anamnese dto);

    void updateEntityFromRequest(ReqAtualizarAnamneseDTO dto, @MappingTarget Anamnese anamnese);
}
