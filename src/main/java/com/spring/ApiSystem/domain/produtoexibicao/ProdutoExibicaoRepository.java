package com.spring.ApiSystem.domain.produtoexibicao;

import com.spring.ApiSystem.domain.horariopersonal.HorarioAgendadoProjection;

import com.spring.ApiSystem.domain.produtoexibicao.enums.ProdutoExibicaoStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ProdutoExibicaoRepository extends JpaRepository<ProdutoExibicao, Long> {
    List<ProdutoExibicao> findByStatus(ProdutoExibicaoStatus produtoExibicaoStatus);
    Integer countByStatus(ProdutoExibicaoStatus produtoExibicaoStatus);

    @Query("SELECT a.data as dataInicio, p.tipoAula, a.status " +
            "FROM agendamento a " +
            "JOIN a.produtoContratado pc " +
            "JOIN pc.produtoExibicao p " +
            "WHERE a.personal.id = :personalId " +
            "AND a.data BETWEEN :start AND :end " +
            "AND a.status IN ('PENDENTE_PERSONAL_APROVACAO', 'PENDENTE_CLIENTE_APROVACAO', 'APROVADO')")
    List<HorarioAgendadoProjection> findAgendamentoSlotsByPersonalIdAndDataBetween(
            @Param("personalId") Long personalId,
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end
    );

    @Query("SELECT CASE WHEN COUNT(p) > 0 THEN true ELSE false END " +
            "FROM produto_exibicao p " +
            "WHERE p.id = :id " +
            "AND p.status = ProdutoExibicaoStatus.ATIVO")
    boolean existsProdutoExibicaoAtivoById(@Param("id") Long id);

}