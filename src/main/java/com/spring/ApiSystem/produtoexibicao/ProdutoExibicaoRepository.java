package com.spring.ApiSystem.produtoexibicao;

import com.spring.ApiSystem.agendamento.dto.response.HorarioAgendadoProjectionDto;
import com.spring.ApiSystem.horariopersonal.HorarioAgendadoProjection;
import com.spring.ApiSystem.produtoexibicao.dto.response.ResProdutoExibicaoDto;
import com.spring.ApiSystem.produtoexibicao.enums.ProdutoExibicaoStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ProdutoExibicaoRepository extends JpaRepository<ProdutoExibicao, Long> {
    List<ProdutoExibicao> findByStatus(ProdutoExibicaoStatus produtoExibicaoStatus);

    @Query("SELECT a.data, p.tipoAula, a.status " +
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
}