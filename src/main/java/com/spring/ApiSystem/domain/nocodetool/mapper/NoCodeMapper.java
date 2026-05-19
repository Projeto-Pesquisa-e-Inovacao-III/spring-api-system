package com.spring.ApiSystem.domain.nocodetool.mapper;

import com.spring.ApiSystem.domain.nocodetool.NoCode;
import com.spring.ApiSystem.domain.nocodetool.dto.request.ReqAtualizarNoCodeDTO;
import com.spring.ApiSystem.domain.nocodetool.dto.request.ReqCriarNoCodeDTO;
import com.spring.ApiSystem.domain.nocodetool.dto.request.ReqRenomearNoCodeDTO;
import com.spring.ApiSystem.domain.nocodetool.dto.response.ResBuscarNoCodeDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface NoCodeMapper {

    @Mapping(target = "user", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "restoredAt", ignore = true)
    @Mapping(target = "restoredFromId", ignore = true)
    NoCode toEntity(ReqCriarNoCodeDTO req);

    @Mapping(target = "user", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "restoredAt", ignore = true)
    @Mapping(target = "restoredFromId", ignore = true)
    NoCode toEntity(ReqAtualizarNoCodeDTO req);

    ResBuscarNoCodeDTO toResBuscarNoCodeDTO(NoCode content);

    ReqCriarNoCodeDTO toReqCriarNoCodeDTO(NoCode content);

    ReqAtualizarNoCodeDTO toReqAtualizarNoCodeDTO(NoCode content);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "description", ignore = true)
    @Mapping(target = "content", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "restoredAt", ignore = true)
    @Mapping(target = "restoredFromId", ignore = true)
    void updateNoCodeFromRenameDto(ReqRenomearNoCodeDTO dto, @MappingTarget NoCode noCode);
}
