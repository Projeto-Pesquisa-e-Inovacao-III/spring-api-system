package com.spring.ApiSystem.repository;

import com.spring.ApiSystem.model.ProdutoContratado;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProdutoContratadoRepository  extends JpaRepository<ProdutoContratado, Integer> {
}
