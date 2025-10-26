package com.spring.ApiSystem.mapper;

import com.spring.ApiSystem.dto.ProdutoContratado.response.ProdutoContratadoDto;
import com.spring.ApiSystem.dto.ProdutoContratado.response.ProdutoContratadoSoComProdutoDto;
import com.spring.ApiSystem.model.ProdutoContratado;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring")
public interface ProdutoContratadoMapper {
    ProdutoContratadoDto toDto(ProdutoContratado produtoContratado);

    ProdutoContratadoSoComProdutoDto toSoComProdutoDto(ProdutoContratado produtoContratado);

    ProdutoContratado toEntity(ProdutoContratadoDto ProdutoContratadoDto);

    ProdutoContratado toEntity(ProdutoContratadoSoComProdutoDto produtoContratadoSoComProdutoDto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    ProdutoContratado partialUpdate(ProdutoContratadoSoComProdutoDto produtoContratadoSoComProdutoDto, @MappingTarget ProdutoContratado produtoContratado);
}
