package com.spring.ApiSystem.produtocontratado.mapper;

import com.spring.ApiSystem.produtocontratado.dto.response.ProdutoContratadoDto;
import com.spring.ApiSystem.produtocontratado.dto.response.ProdutoContratadoSoComProdutoDto;
import com.spring.ApiSystem.produtocontratado.ProdutoContratado;
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
