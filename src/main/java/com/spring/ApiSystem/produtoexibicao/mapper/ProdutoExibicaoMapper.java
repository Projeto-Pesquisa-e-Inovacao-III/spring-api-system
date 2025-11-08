package com.spring.ApiSystem.produtoexibicao.mapper;

import com.spring.ApiSystem.enums.Status;
import com.spring.ApiSystem.produtoexibicao.dto.request.CadastroProdutoExibicaoDTO;
import com.spring.ApiSystem.produtoexibicao.dto.response.ProdutoExibicaoDto;
import com.spring.ApiSystem.produtoexibicao.ProdutoExibicao;
import com.spring.ApiSystem.produtoexibicao.dto.response.ResProdutoExibicaoDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ProdutoExibicaoMapper {
    ProdutoExibicaoDto toDto(ProdutoExibicao produtoExibicao);
    ProdutoExibicao toEntity(ProdutoExibicaoDto dto);
    ProdutoExibicao toEntity(CadastroProdutoExibicaoDTO cadastroProdutoExibicaoDTO);
    ResProdutoExibicaoDTO toResProdutoExibicaoDTO(ProdutoExibicao produtoExibicao);

    default Status mapStatus(String status) {
        return Status.valueOf(status.toUpperCase());
    }
}
