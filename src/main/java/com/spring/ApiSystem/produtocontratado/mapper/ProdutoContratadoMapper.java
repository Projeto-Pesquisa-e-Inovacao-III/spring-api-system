package com.spring.ApiSystem.produtocontratado.mapper;

import com.spring.ApiSystem.produtocontratado.dto.request.ReqOperacaoSaldoDto;
import com.spring.ApiSystem.produtocontratado.dto.response.*;
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

    @Mapping(target = "tipoAula", source = "produtoExibicao.tipoAula")
    ResBuscarSaldoPorTipoAulaDto toBuscarSaldoPorTipoAulaDto(ProdutoContratado produtoContratado);

    ResProdutoContratadoDto toDto(ProdutoContratado produtoContratado);
    List<ResProdutoContratadoDto> toListDto(List<ProdutoContratado> produtoContratado);

    ProdutoContratado toEntity(ResProdutoContratadoDto ResProdutoContratadoDto);
    ProdutoContratado toEntity(ResBuscarProdutoContratadoPorIdDto buscarProdutoContratadoPorIdDto);

    @Mapping(target = "nomeComprador", source = "aluno.nome")
    @Mapping(target = "emailComprador", source = "aluno.email")
    @Mapping(target = "telefone", expression = "java(formatarTelefone(produtoContratado))")
    @Mapping(target = "cpf", source = "aluno.cpf")
    @Mapping(target = "produtoComprado", source = "produtoExibicao.titulo")
    @Mapping(target = "valorCompra", source = "produtoExibicao.preco")
    ResProdutoContratadoDetalhadoDTO toResProdutoContratadoDetalhadoDTO(ProdutoContratado produtoContratado);


    default String formatarTelefone(ProdutoContratado produtoContratado) {
        if (produtoContratado.getAluno().getTelefones().isEmpty()) {
            return null;
        }
        var telefone = produtoContratado.getAluno().getTelefones().get(0);
        return telefone.getDdd() + telefone.getNumero();
    }
}
