package com.spring.ApiSystem.domain.agendamento;

import com.spring.ApiSystem.domain.agendamento.projection.ResTotalAgendamentoByStatusProjection;
import com.spring.ApiSystem.domain.agendamento.enums.AgendamentoStatus;
import com.spring.ApiSystem.domain.produtoexibicao.enums.TipoAula;
import com.spring.ApiSystem.shared.enums.DiaSemana;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface AgendamentoRepository extends JpaRepository<Agendamento, Long> {

    /* -------------------- Buscas simples -------------------- */


    List<Agendamento> findAgendamentoByPersonal_Id(Long personalId);

    List<Agendamento> findAgendamentoByAluno_Id(Long alunoId);

    @Query("""
        SELECT a FROM agendamento a
        WHERE a.personal.id = :personalId
          AND a.diaSemana = :diaSemana
          AND a.data >= CURRENT_TIMESTAMP
    """)
    Page<Agendamento> findByPersonalIdAndDiaSemana(
            Pageable pageable,
            @Param("personalId") Long personalId,
            @Param("diaSemana") DiaSemana diaSemana
    );
    

    @Query("SELECT a FROM agendamento a " +
            "WHERE a.personal.id = :personalId " +
            "  AND (:nomeDoAluno IS NULL OR a.aluno.nome LIKE %:nomeDoAluno%) " +
            "  AND (:status IS NULL OR a.status = :status) " +
            "  AND (:tipoAgendamento IS NULL OR a.produtoContratado.produtoExibicao.tipoAula = :tipoAgendamento) " +
            "  AND (:dataInic IS NULL OR a.data >= :dataInic) " +
            "  AND (:dataFim IS NULL OR a.data <= :dataFim) " +
            "ORDER BY a.data ASC")
    Page<Agendamento> findByPersonalIdOrderByDataAsc(
            @Param("personalId") Long personalId,
            @Param("nomeDoAluno") String nomeDoAluno,
            @Param("status") AgendamentoStatus status,
            @Param("tipoAgendamento") TipoAula tipoAgendamento,
            @Param("dataInic") LocalDateTime dataInic,
            @Param("dataFim") LocalDateTime dataFim,
            Pageable pageable);

    Page<Agendamento> findByAlunoIdOrderByDataAsc(Long alunoId, Pageable pageable);

    /* -------------------- Consultas com filtros/custom -------------------- */

    @Query("SELECT a FROM agendamento a " +
            "LEFT JOIN FETCH a.produtoContratado pc " +
            "LEFT JOIN FETCH pc.produtoExibicao pe " +
            "LEFT JOIN FETCH a.personal p " +
            "LEFT JOIN FETCH a.endereco e " +
            "WHERE a.aluno.id = :alunoId " +
            "  AND a.status = 'APROVADO' " +
            "  AND a.data >= CURRENT_TIMESTAMP " +
            "ORDER BY a.data ASC " +
            "LIMIT 4")
    List<Agendamento> buscarAgendamentosMaisProximosPorAluno(@Param("alunoId") Long alunoId);

    @Query("SELECT a FROM agendamento a " +
            "LEFT JOIN FETCH a.produtoContratado pc " +
            "LEFT JOIN FETCH pc.produtoExibicao pe " +
            "LEFT JOIN FETCH a.aluno al " +
            "LEFT JOIN FETCH a.endereco e " +
            "WHERE a.personal.id = :personalId " +
            "  AND a.status = 'APROVADO' " +
            "  AND a.data >= CURRENT_TIMESTAMP " +
            "ORDER BY a.data ASC " +
            "LIMIT 4")
    List<Agendamento> buscarAgendamentosMaisProximosPorPersonal(@Param("personalId") Long personalId);

    @Query("SELECT a FROM agendamento a " +
            "LEFT JOIN FETCH a.produtoContratado pc " +
            "LEFT JOIN FETCH pc.produtoExibicao pe " +
            "LEFT JOIN FETCH a.personal p " +
            "LEFT JOIN FETCH a.aluno al " +
            "LEFT JOIN FETCH a.endereco e " +
            "WHERE a.id = :agendamentoId " +
            "  AND (al.email = :email " +
            "       OR p.email = :email)")
    Optional<Agendamento> buscarPorIdEEmailDoUsuario(
            @Param("agendamentoId") Long agendamentoId,
            @Param("email") String email
    );

    @Query("SELECT a.id FROM agendamento a WHERE a.data <= :agora AND a.status = :status ORDER BY a.id")
    List<Long> buscarIdsPorDataAnteriorEStatus(
            @Param("agora") LocalDateTime agora,
            @Param("status") AgendamentoStatus status,
            Pageable pageable
    );

    /* -------------------- Modificações -------------------- */

    @Modifying
    @Transactional
    @Query("UPDATE agendamento a SET a.status = :novoStatus WHERE a.id IN :ids")
    int atualizarStatusPorIds(
            @Param("novoStatus") AgendamentoStatus novoStatus,
            @Param("ids") List<Long> ids
    );

    @Modifying
    @Transactional
    @Query("UPDATE agendamento a SET a.status = :novoStatus " +
            "WHERE ((:tipo = 'ALUNO' AND a.aluno.id = :idUsuario) " +
            "   OR (:tipo = 'PERSONAL' AND a.personal.id = :idUsuario)) " +
            "  AND a.status <> :novoStatus " +
            "  AND a.data > CURRENT_TIMESTAMP")
    void cancelarTodosAgendamentosPorUsuario(
            @Param("idUsuario") Long idUsuario,
            @Param("tipo") String tipo,
            @Param("novoStatus") AgendamentoStatus novoStatus
    );

    /* -------------------- Verificações de conflito -------------------- */

    @Query("SELECT CASE WHEN COUNT(a) > 0 THEN true ELSE false END FROM agendamento a " +
            "WHERE a.aluno.id = :alunoId " +
            "  AND a.personal.id = :personalId " +
            "  AND a.status NOT IN (AgendamentoStatus.CANCELADO_CLIENTE, AgendamentoStatus.CANCELADO_PERSONAL) " +
            "  AND a.data < :dataFim " +
            "  AND a.dataFim > :dataInicio")
    boolean existeConflito(
            @Param("alunoId") Long alunoId,
            @Param("personalId") Long personalId,
            @Param("dataInicio") LocalDateTime dataInicio,
            @Param("dataFim") LocalDateTime dataFim
    );

    @Query("SELECT CASE WHEN COUNT(a) > 0 THEN true ELSE false END FROM agendamento a " +
            "WHERE a.aluno.id = :alunoId " +
            "  AND a.personal.id = :personalId " +
            "  AND a.id <> :agendamentoId " +
            "  AND a.status NOT IN (AgendamentoStatus.CANCELADO_CLIENTE, AgendamentoStatus.CANCELADO_PERSONAL) " +
            "  AND a.data < :dataFim " +
            "  AND a.dataFim > :dataInicio")
    boolean existeConflitoExcluindoAgendamento(
            @Param("alunoId") Long alunoId,
            @Param("personalId") Long personalId,
            @Param("dataInicio") LocalDateTime dataInicio,
            @Param("dataFim") LocalDateTime dataFim,
            @Param("agendamentoId") Long agendamentoId
    );

    /* -------------------- Relatórios / Agregações -------------------- */

    @Query("SELECT MONTH(a.data) as mes," +
            "YEAR(a.data) as ano," +
            "COUNT(a) FROM agendamento a" +
            " WHERE a.personal.id = :personalId AND" +
            " a.status = :status" +
            " GROUP BY ano, mes" +
            " ORDER BY ano DESC, mes DESC" +
            " LIMIT :quantidadeMeses")
    List<Object[]> listarConsultoriasRealizadasMes(
            @Param("personalId") Long personalId,
            @Param("status") AgendamentoStatus status,
            @Param("quantidadeMeses") Integer quantidadeMeses
    );

    @Query("""
            SELECT COUNT(a) FROM agendamento a
            WHERE a.personal.id = :personalId
            AND a.status = :status
            AND (:data IS NULL OR CAST(a.data AS DATE) = :data)
    """)
    Integer countByPersonalIdAndStatusAndOptionalData(
            @Param("personalId") Long personalId,
            @Param("status") AgendamentoStatus status,
            @Param("data") LocalDate data
    );

    @Query("""
        SELECT COUNT(a)
        FROM agendamento a
        WHERE a.personal.id = :personalId
          AND a.status IN (:status1, :status2)
    """)
    Integer somarValorPorPersonalEStatus(
            @Param("personalId") Long personalId,
            @Param("status") AgendamentoStatus status
    );

    @Query("""
        SELECT
            COALESCE(SUM(CASE WHEN a.status IN (
                PENDENTE_PERSONAL_APROVACAO,
                PENDENTE_PERSONAL_CONCLUIR
            ) THEN 1 ELSE 0 END), 0) as totalPendente,
    
            COALESCE(SUM(CASE WHEN a.status IN (
                PENDENTE_CLIENTE_APROVACAO,
                APROVADO
            ) THEN 1 ELSE 0 END), 0) as totalRespondido,
    
            COALESCE(SUM(CASE WHEN a.status IN (
                CANCELADO_CLIENTE,
                CANCELADO_PERSONAL
            )
            AND YEAR(a.data) = YEAR(CURRENT_DATE)
            AND MONTH(a.data) = MONTH(CURRENT_DATE)
            THEN 1 ELSE 0 END), 0) as totalCanceladoPorMesAtual
        FROM agendamento a
        WHERE a.personal.id = :personalId
    """)
    ResTotalAgendamentoByStatusProjection countTotalStatusAgendamentoByPersonal(@Param("personalId") Long personalId);

}
