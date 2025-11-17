package com.spring.ApiSystem.produtocontratado.mapper;

import com.spring.ApiSystem.produtocontratado.dto.request.ReqOperacaoSaldoDto;
import com.spring.ApiSystem.produtocontratado.dto.response.ResBuscarProdutoContratadoPorIdDto;
import com.spring.ApiSystem.produtocontratado.dto.response.ResOperacaoSaldoDto;
import com.spring.ApiSystem.produtocontratado.dto.response.ResProdutoContratadoAtivoDto;
import com.spring.ApiSystem.produtocontratado.dto.response.ResProdutoContratadoDto;
import com.spring.ApiSystem.produtocontratado.ProdutoContratado;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ProdutoContratadoMapper {

    @Mapping(target = "alunoId", source = "aluno.id")
    @Mapping(target = "produtoExibicaoId", source = "produtoExibicao.id")
    ResBuscarProdutoContratadoPorIdDto toBuscarProdutoContratadoPorIdDto(ProdutoContratado produtoContratado);
    ResOperacaoSaldoDto toOperacaoSaldoDto(ProdutoContratado produtoContratado);

    @Mapping(target = "nome", source = "produtoExibicao.titulo")
    ResProdutoContratadoAtivoDto toResProdutoContratadoAtivoDto(ProdutoContratado produtoContratado);

    ResProdutoContratadoDto toDto(ProdutoContratado produtoContratado);
    List<ResProdutoContratadoDto> toListDto(List<ProdutoContratado> produtoContratado);

    ProdutoContratado toEntity(ResProdutoContratadoDto ResProdutoContratadoDto);
    ProdutoContratado toEntity(ResBuscarProdutoContratadoPorIdDto buscarProdutoContratadoPorIdDto);

}
