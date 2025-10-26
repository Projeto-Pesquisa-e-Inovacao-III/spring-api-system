package com.spring.ApiSystem.mapper;

import com.spring.ApiSystem.dto.produtoExibicao.request.CadastroProdutoExibicaoDTO;
import com.spring.ApiSystem.dto.produtoExibicao.response.ResProdutoExibicaoDTO;
import com.spring.ApiSystem.enums.Status;
import com.spring.ApiSystem.model.ProdutoExibicao;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ProdutoExibicaoMapper {
    ProdutoExibicao toEntity(CadastroProdutoExibicaoDTO cadastroProdutoExibicaoDTO);
    ResProdutoExibicaoDTO toResProdutoExibicaoDTO(ProdutoExibicao produtoExibicao);

    default Status mapStatus(String status) {
        return Status.valueOf(status.toUpperCase());
    }
}
