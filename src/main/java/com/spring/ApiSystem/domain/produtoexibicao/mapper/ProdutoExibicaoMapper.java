package com.spring.ApiSystem.domain.produtoexibicao.mapper;

import com.spring.ApiSystem.domain.beneficio.mapper.BeneficioMapper;
import com.spring.ApiSystem.domain.produtoexibicao.dto.request.ReqEdicaoProdutoExibicaoDto;
import com.spring.ApiSystem.domain.produtoexibicao.dto.request.ReqCadastroProdutoExibicaoDto;
import com.spring.ApiSystem.domain.produtoexibicao.dto.response.ProdutoExibicaoDto;
import com.spring.ApiSystem.domain.produtoexibicao.ProdutoExibicao;
import com.spring.ApiSystem.domain.produtoexibicao.dto.response.ResProdutoExibicaoDto;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring", uses = {BeneficioMapper.class})
public interface ProdutoExibicaoMapper {
    ProdutoExibicaoDto toDto(ProdutoExibicao produtoExibicao);
    ProdutoExibicao toEntity(ProdutoExibicaoDto dto);

    @AfterMapping
    default void linkBeneficios(@MappingTarget ProdutoExibicao produto) {
        if (produto.getBeneficios() != null) {
            produto.getBeneficios().forEach(beneficio ->
                    beneficio.setProdutoExibicao(produto)  // ← Define a FK
            );
        }
    }

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "dataCriacao", ignore = true)
    ProdutoExibicao toEntity(ReqCadastroProdutoExibicaoDto reqCadastroProdutoExibicaoDTO);

    ProdutoExibicao toEntity(ReqEdicaoProdutoExibicaoDto dto);
    ResProdutoExibicaoDto toResProdutoExibicaoDTO(ProdutoExibicao produtoExibicao);

    ReqCadastroProdutoExibicaoDto toCadastroProdutoExibicaoDTO(ReqEdicaoProdutoExibicaoDto produtoExibicao);
    List<ResProdutoExibicaoDto> toResProdutoExibicaoDTOList(List<ProdutoExibicao> produtoExibicoes);


}
