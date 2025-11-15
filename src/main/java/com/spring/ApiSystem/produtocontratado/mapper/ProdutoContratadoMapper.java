package com.spring.ApiSystem.produtocontratado.mapper;

import com.spring.ApiSystem.produtocontratado.dto.response.ResBuscarProdutoContratadoPorIdDto;
import com.spring.ApiSystem.produtocontratado.dto.response.ResOperacaoSaldoDto;
import com.spring.ApiSystem.produtocontratado.dto.response.ResProdutoContratadoDto;
import com.spring.ApiSystem.produtocontratado.ProdutoContratado;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ProdutoContratadoMapper {
    ResProdutoContratadoDto toDto(ProdutoContratado produtoContratado);
    List<ResProdutoContratadoDto> toListDto(List<ProdutoContratado> produtoContratado);
    @Mapping(target = "alunoId", source = "aluno.id")
    @Mapping(target = "produtoExibicaoId", source = "produtoExibicao.id")
    ResBuscarProdutoContratadoPorIdDto toBuscarProdutoContratadoPorIdDto(ProdutoContratado produtoContratado);
    ResOperacaoSaldoDto toOperacaoSaldoDto(ProdutoContratado produtoContratado);

    ProdutoContratado toEntity(ResProdutoContratadoDto ResProdutoContratadoDto);
    ProdutoContratado toEntity(ResBuscarProdutoContratadoPorIdDto buscarProdutoContratadoPorIdDto);
}
