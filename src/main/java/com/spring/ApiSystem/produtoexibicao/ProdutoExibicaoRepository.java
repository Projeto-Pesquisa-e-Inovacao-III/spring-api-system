package com.spring.ApiSystem.produtoexibicao;

import com.spring.ApiSystem.produtoexibicao.enums.ProdutoExibicaoStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProdutoExibicaoRepository extends JpaRepository<ProdutoExibicao, Long> {
    List<ProdutoExibicao> findByStatus(ProdutoExibicaoStatus produtoExibicaoStatus);
}