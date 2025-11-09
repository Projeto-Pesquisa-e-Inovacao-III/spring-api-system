package com.spring.ApiSystem.produtocontratado.mapper;

import com.spring.ApiSystem.produtocontratado.dto.request.EditarProdutoContratadoDto;
import com.spring.ApiSystem.produtocontratado.dto.response.BuscarProdutoContratadoPorIdDto;
import com.spring.ApiSystem.produtocontratado.dto.response.ResOperacaoSaldoDto;
import com.spring.ApiSystem.produtocontratado.dto.response.ProdutoContratadoDto;
import com.spring.ApiSystem.produtocontratado.dto.response.ProdutoContratadoSoComProdutoDto;
import com.spring.ApiSystem.produtocontratado.ProdutoContratado;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ProdutoContratadoMapper {
    ProdutoContratadoDto toDto(ProdutoContratado produtoContratado);
    List<ProdutoContratadoDto> toListDto(List<ProdutoContratado> produtoContratado);
    ProdutoContratadoSoComProdutoDto toSoComProdutoDto(ProdutoContratado produtoContratado);
    @Mapping(target = "alunoId", source = "aluno.id")
    @Mapping(target = "produtoExibicaoId", source = "produtoExibicao.id")
    BuscarProdutoContratadoPorIdDto toBuscarProdutoContratadoPorIdDto(ProdutoContratado produtoContratado);
    ResOperacaoSaldoDto toOperacaoSaldoDto(ProdutoContratado produtoContratado);

    ProdutoContratado toEntity(ProdutoContratadoDto ProdutoContratadoDto);
    ProdutoContratado toEntity(ProdutoContratadoSoComProdutoDto produtoContratadoSoComProdutoDto);
    ProdutoContratado toEntity(BuscarProdutoContratadoPorIdDto buscarProdutoContratadoPorIdDto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    ProdutoContratado partialUpdate(ProdutoContratadoSoComProdutoDto produtoContratadoSoComProdutoDto, @MappingTarget ProdutoContratado produtoContratado);

    @Mapping(target = "aluno", ignore = true)
    @Mapping(target = "produtoExibicao", ignore = true)
    void partialUpdate(EditarProdutoContratadoDto editarProdutoContratadoDto, @MappingTarget ProdutoContratado produtoContratado);
}
