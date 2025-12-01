package com.spring.ApiSystem.produtocontratado;

import com.spring.ApiSystem.produtoexibicao.enums.TipoAula;
import com.spring.ApiSystem.produtoexibicao.enums.TipoProduto;
import jakarta.persistence.LockModeType;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface ProdutoContratadoRepository  extends JpaRepository<ProdutoContratado, Long> {
    List<ProdutoContratado> findBySituacao(Boolean status);
    List<ProdutoContratado> findByAlunoEmail(String email, Pageable pageable);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT p FROM produto_contratado p WHERE p.id = :id")
    ProdutoContratado findByIdWithLock(Long id);


    @Query("SELECT pc FROM produto_contratado pc WHERE pc.aluno.id = :alunoId " +
            "AND pc.produtoExibicao.tipoAula = :tipoAula AND pc.situacao = true " +
            "AND pc.saldoAula > 1 ORDER BY pc.dataExpiracao ASC")
    Optional<ProdutoContratado> findFirstByAlunoIdAndTipoAulaWithSaldoGreaterThanOne(
            @Param("alunoId") Long alunoId, @Param("tipoAula") TipoAula tipoAula);

    @Query("SELECT pc FROM produto_contratado pc, agendamento a " +
            "WHERE a.produtoContratado = pc AND a.id = :agendamentoId")
    ProdutoContratado findByAgendamentoId(@Param("agendamentoId") Long agendamentoId);

    Optional<ProdutoContratado> findByIdAndAlunoEmail(Long id, String email);

    @Query("""
       SELECT pc
         FROM produto_contratado pc
        WHERE pc.situacao = true
          AND pc.aluno.email = :email
       """)
    Optional<ProdutoContratado> buscarProdutoContratadoAtivo(@Param("email") String email);

    @Query("""
       SELECT COALESCE(SUM(pc.saldoAula), 0)
         FROM produto_contratado pc
        WHERE pc.situacao = true
          AND pc.produtoExibicao.tipoAula = :tipoAula
       """)
    Integer totalSaldoAtivoPorTipo(@Param("tipoAula") TipoAula tipoAula);

    @Query("""
    SELECT MONTH(pc.dataCompra) as mes,
           YEAR(pc.dataCompra) as ano,
           SUM(pe.preco) as totalPreco
      FROM produto_contratado pc
      JOIN produto_exibicao pe ON pc.produtoExibicao.id = pe.id
     GROUP BY ano, mes
     ORDER BY ano DESC, mes DESC
     LIMIT :quantidadeMeses
    """)
    List<Object[]> listarGanhosPorMesDeCompra(@Param("quantidadeMeses") Integer quantidadeMeses);

    @Query("""
       SELECT COUNT(pc)
         FROM produto_contratado pc
        WHERE pc.dataCompra >= :dataInicio AND
        pc.dataCompra <= :dataFinal
     """)
    Integer totalPlanosVendidosUltimosDias(@Param("dataInicio") LocalDate dataInicio,
                                           @Param("dataFinal") LocalDate dataFinal);

    @Query("""
    SELECT COUNT(DISTINCT a.id)
        FROM Aluno a
        LEFT JOIN produto_contratado pc ON pc.aluno.id = a.id
                                 AND pc.situacao = true
                                 AND pc.produtoExibicao.tipoProduto = :tipoProduto
                                 AND pc.dataExpiracao >= now()
    WHERE a.ativo = true
        AND pc.id IS NULL
    """)
    Integer countAlunosComPlanosExpirados(@Param("tipoProduto") TipoProduto tipoProduto);

}
