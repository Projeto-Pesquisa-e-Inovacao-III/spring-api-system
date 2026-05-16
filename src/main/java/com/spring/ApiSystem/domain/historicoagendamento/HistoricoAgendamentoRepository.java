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
            COALESCE(SUM(CASE WHEN h.status IN (
                PENDENTE_PERSONAL_APROVACAO,
                PENDENTE_PERSONAL_CONCLUIR
            ) THEN 1 ELSE 0 END), 0) as totalPendente,

            COALESCE(SUM(CASE WHEN h.status IN (
                PENDENTE_CLIENTE_APROVACAO,
                APROVADO
            ) THEN 1 ELSE 0 END), 0) as totalRespondido,

            COALESCE(SUM(CASE WHEN h.status IN (
              CANCELADO_CLIENTE,
              CANCELADO_PERSONAL
            ) THEN 1 ELSE 0 END), 0) as totalCanceladoPorMesAtual
        FROM historico_agendamento h
        WHERE h.agendamento.personal.id = :personalId
          AND YEAR(h.dataCriacao) = YEAR(CURRENT_DATE)
          AND MONTH(h.dataCriacao) = MONTH(CURRENT_DATE)
    """)
    ResTotalAgendamentoByStatusProjection countTotalStatusAgendamentoByPersonal(@Param("personalId") Long personalId);
}
