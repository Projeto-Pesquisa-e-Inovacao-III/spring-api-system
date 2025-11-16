package com.spring.ApiSystem.produtoexibicao.mapper;

import com.spring.ApiSystem.produtoexibicao.dto.request.EdicaoProdutoExibicaoDTO;
import com.spring.ApiSystem.produtoexibicao.dto.response.ResListaProdutoExibicaoDto;
import com.spring.ApiSystem.produtoexibicao.enums.Status;
import com.spring.ApiSystem.produtoexibicao.dto.request.CadastroProdutoExibicaoDTO;
import com.spring.ApiSystem.produtoexibicao.dto.response.ProdutoExibicaoDto;
import com.spring.ApiSystem.produtoexibicao.ProdutoExibicao;
import com.spring.ApiSystem.produtoexibicao.dto.response.ResProdutoExibicaoDto;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ProdutoExibicaoMapper {
    ProdutoExibicaoDto toDto(ProdutoExibicao produtoExibicao);
    ProdutoExibicao toEntity(ProdutoExibicaoDto dto);
    ProdutoExibicao toEntity(CadastroProdutoExibicaoDTO cadastroProdutoExibicaoDTO);
    ProdutoExibicao toEntity(EdicaoProdutoExibicaoDTO dto);
    ResProdutoExibicaoDto toResProdutoExibicaoDTO(ProdutoExibicao produtoExibicao);
    List<ResProdutoExibicaoDto> toResProdutoExibicaoDTO(List<ProdutoExibicao> produtoExibicao);
    List<ResListaProdutoExibicaoDto> toResListaProdutoExibicaoDTO(List<ProdutoExibicao> produtoExibicao);
    CadastroProdutoExibicaoDTO toCadastroProdutoExibicaoDTO(EdicaoProdutoExibicaoDTO produtoExibicao);

    default Status mapStatus(String status) {
        return Status.valueOf(status.toUpperCase());
    }
}
