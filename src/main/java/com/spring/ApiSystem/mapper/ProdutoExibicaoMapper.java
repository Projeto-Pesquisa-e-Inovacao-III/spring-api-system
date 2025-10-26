package com.spring.ApiSystem.mapper;

import com.spring.ApiSystem.dto.ProdutoExibicao.response.ProdutoExibicaoDto;
import com.spring.ApiSystem.model.ProdutoContratado;
import com.spring.ApiSystem.model.ProdutoExibicao;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ProdutoExibicaoMapper {
    ProdutoExibicaoDto toDto(ProdutoExibicao produtoExibicao);
    ProdutoExibicao toEntity(ProdutoExibicaoDto dto);
}
