package com.spring.ApiSystem.produtoexibicao.mapper;

import com.spring.ApiSystem.produtoexibicao.dto.request.EdicaoProdutoExibicaoDTO;
import com.spring.ApiSystem.produtoexibicao.dto.response.ResListaProdutoExibicaoDTO;
import com.spring.ApiSystem.produtoexibicao.enums.Status;
import com.spring.ApiSystem.produtoexibicao.dto.request.CadastroProdutoExibicaoDTO;
import com.spring.ApiSystem.produtoexibicao.dto.response.ProdutoExibicaoDto;
import com.spring.ApiSystem.produtoexibicao.ProdutoExibicao;
import com.spring.ApiSystem.produtoexibicao.dto.response.ResProdutoExibicaoDTO;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ProdutoExibicaoMapper {
    ProdutoExibicaoDto toDto(ProdutoExibicao produtoExibicao);
    ProdutoExibicao toEntity(ProdutoExibicaoDto dto);
    ProdutoExibicao toEntity(CadastroProdutoExibicaoDTO cadastroProdutoExibicaoDTO);
    ProdutoExibicao toEntity(EdicaoProdutoExibicaoDTO dto);
    ResProdutoExibicaoDTO toResProdutoExibicaoDTO(ProdutoExibicao produtoExibicao);
    List<ResProdutoExibicaoDTO> toResProdutoExibicaoDTO(List<ProdutoExibicao> produtoExibicao);
    List<ResListaProdutoExibicaoDTO> toResListaProdutoExibicaoDTO(List<ProdutoExibicao> produtoExibicao);
    CadastroProdutoExibicaoDTO toCadastroProdutoExibicaoDTO(EdicaoProdutoExibicaoDTO produtoExibicao);

    default Status mapStatus(String status) {
        return Status.valueOf(status.toUpperCase());
    }
}
