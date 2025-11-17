package com.spring.ApiSystem.produtoexibicao.mapper;

import com.spring.ApiSystem.produtoexibicao.dto.request.ReqEdicaoProdutoExibicaoDTO;
import com.spring.ApiSystem.produtoexibicao.dto.request.ReqCadastroProdutoExibicaoDTO;
import com.spring.ApiSystem.produtoexibicao.dto.response.ProdutoExibicaoDto;
import com.spring.ApiSystem.produtoexibicao.ProdutoExibicao;
import com.spring.ApiSystem.produtoexibicao.dto.response.ResProdutoExibicaoDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ProdutoExibicaoMapper {
    ProdutoExibicaoDto toDto(ProdutoExibicao produtoExibicao);
    ProdutoExibicao toEntity(ProdutoExibicaoDto dto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "dataCriacao", ignore = true)
    ProdutoExibicao toEntity(ReqCadastroProdutoExibicaoDTO reqCadastroProdutoExibicaoDTO);

    ProdutoExibicao toEntity(ReqEdicaoProdutoExibicaoDTO dto);
    ResProdutoExibicaoDTO toResProdutoExibicaoDTO(ProdutoExibicao produtoExibicao);

    ReqCadastroProdutoExibicaoDTO toCadastroProdutoExibicaoDTO(ReqEdicaoProdutoExibicaoDTO produtoExibicao);
  List  <ResProdutoExibicaoDTO> toResProdutoExibicaoDTOList(List<ProdutoExibicao> produtoExibicaos);


}
