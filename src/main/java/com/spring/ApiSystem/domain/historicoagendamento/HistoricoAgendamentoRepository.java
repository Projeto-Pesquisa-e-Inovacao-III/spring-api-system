// java
package com.spring.ApiSystem.domain.historicoagendamento;

import com.spring.ApiSystem.domain.agendamento.projection.ResTotalAgendamentoByStatusProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface HistoricoAgendamentoRepository extends JpaRepository<HistoricoAgendamento, Long> {

    @Query("""
        SELECT
            COALESCE(SUM(CASE WHEN h.status = PENDENTE_PERSONAL_APROVACAO THEN 1 ELSE 0 END), 0) as totalPendente,
            COALESCE(SUM(CASE WHEN h.status = APROVADO THEN 1 ELSE 0 END), 0) as totalRespondido,
            COALESCE(SUM(CASE WHEN h.status IN (
                CANCELADO_CLIENTE,
                CANCELADO_PERSONAL
            ) THEN 1 ELSE 0 END), 0) as totalCanceladoPorMesAtual
        FROM historico_agendamento h
        WHERE h.dataCriacao = (
            SELECT MAX(h2.dataCriacao)
            FROM historico_agendamento h2
            WHERE h2.agendamento = h.agendamento
        )
          AND h.agendamento.personal.id = :personalId
          AND FUNCTION('YEAR', h.dataCriacao) = FUNCTION('YEAR', CURRENT_DATE)
          AND FUNCTION('MONTH', h.dataCriacao) = FUNCTION('MONTH', CURRENT_DATE)
    """)
    ResTotalAgendamentoByStatusProjection countTotalStatusAgendamentoByPersonal(@Param("personalId") Long personalId);
}
