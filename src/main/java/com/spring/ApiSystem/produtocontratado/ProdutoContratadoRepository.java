package com.spring.ApiSystem.produtocontratado;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProdutoContratadoRepository  extends JpaRepository<ProdutoContratado, Integer> {
}
