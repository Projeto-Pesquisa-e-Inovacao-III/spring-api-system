package com.spring.ApiSystem.produtocontratado;

import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
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

    @Query("SELECT pc FROM produto_contratado pc WHERE pc.aluno.id = :alunoId " +
            "AND pc.produtoExibicao.tipoAula = :tipoAula AND pc.situacao = true " +
            "AND pc.saldoAula > 1 ORDER BY pc.dataExpiracao ASC")
    Optional<ProdutoContratado> findFirstByAlunoIdAndTipoAulaWithSaldoGreaterThanOne(
            @Param("alunoId") Long alunoId, @Param("tipoAula") String tipoAula);

    @Query("SELECT pc FROM produto_contratado pc, agendamento a " +
            "WHERE a.produtoContratado = pc AND a.id = :agendamentoId")
    ProdutoContratado findByAgendamentoId(@Param("agendamentoId") Long agendamentoId);
}
