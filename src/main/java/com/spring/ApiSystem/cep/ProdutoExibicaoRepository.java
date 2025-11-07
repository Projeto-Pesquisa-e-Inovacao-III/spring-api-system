package com.spring.ApiSystem.repository;

import com.spring.ApiSystem.dto.produtoExibicao.response.ResProdutoExibicaoDTO;
import com.spring.ApiSystem.enums.Status;
import com.spring.ApiSystem.model.ProdutoExibicao;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProdutoExibicaoRepository extends JpaRepository<ProdutoExibicao, Long> {
    List<ResProdutoExibicaoDTO> findByStatus(Status status);
    List<ResProdutoExibicaoDTO> findAllBy();
}
