package com.spring.ApiSystem.domain.resumoAgendamento;

import com.spring.ApiSystem.domain.agendamento.projection.ResTotalAgendamentoByStatusProjection;
import com.spring.ApiSystem.domain.resumoAgendamento.dto.res.ResResumoDTO;
import com.spring.ApiSystem.domain.resumoAgendamento.projection.ResAgendamentoWithResumeProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ResumoAgendamentoRepository extends JpaRepository<ResumoAgendamento, Long> {

    @Query("""
        SELECT r
        FROM resumo_agendamento r
        WHERE r.aluno.id = :alunoId AND
        r.personal.id = :personalId AND
        (:proximoId is null or r.id > :proximoId)
        ORDER BY r.agendamento.data DESC
    """)
    List<ResumoAgendamento> findByAlunoIdAndPersonalId(Long alunoId, Long personalId,
                                                       Long proximoId, Pageable pageable);

    @Query("""
    SELECT r
    FROM resumo_agendamento r
    INNER JOIN FETCH r.agendamento a
    INNER JOIN FETCH r.aluno al
    INNER JOIN FETCH r.personal p
    WHERE al.id = :alunoId
    """)
    Page<ResumoAgendamento> getResumosWithAgendamentoByAlunoId(@Param("alunoId") Long alunoId, Pageable pageable);
}
