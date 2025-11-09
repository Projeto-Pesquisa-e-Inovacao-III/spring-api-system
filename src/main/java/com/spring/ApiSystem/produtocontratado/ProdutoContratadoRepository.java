package com.spring.ApiSystem.produtocontratado;

import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProdutoContratadoRepository  extends JpaRepository<ProdutoContratado, Long> {
    List<ProdutoContratado> findBySituacao(Boolean situacao);
    List<ProdutoContratado> findByAlunoId(Long idAluno);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT p FROM produto_contratado p WHERE p.id = :id")
    ProdutoContratado findByIdWithLock(Long id);

    List<ProdutoContratado> findByAlunoIdAndProdutoExibicaoTipoAula(Long idAluno, String tipoAula);
}
