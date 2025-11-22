package com.spring.ApiSystem.produtoexibicao;


import com.spring.ApiSystem.produtoexibicao.dto.response.ResProdutoExibicaoDto;
import com.spring.ApiSystem.produtoexibicao.enums.ProdutoExibicaoStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProdutoExibicaoRepository extends JpaRepository<ProdutoExibicao, Long> {
    List<ResProdutoExibicaoDto> findByStatus(ProdutoExibicaoStatus produtoExibicaoStatus);
}