package com.spring.ApiSystem.agendamento;


import com.spring.ApiSystem.agendamento.enums.AgendamentoStatus;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface AgendamentoRepository  extends JpaRepository<Agendamento, Long> {


    Page<Agendamento> findByPersonalIdOrderByDataAsc(Long personalId, Pageable pageable);
    Page<Agendamento>findByAlunoIdOrderByDataAsc(Long alunoId, Pageable pageable);

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
    List<Long> buscarIdsPorDataAnteriorEStatus(@Param("agora") LocalDateTime agora,
                                               @Param("status") AgendamentoStatus status,
                                               Pageable pageable);


    @Modifying
    @Transactional
    @Query("UPDATE agendamento a SET a.status = :novoStatus WHERE a.id IN :ids")
    int atualizarStatusPorIds(@Param("novoStatus") AgendamentoStatus novoStatus,
                              @Param("ids") List<Long> ids);

    @Query("SELECT CASE WHEN COUNT(a) > 0 THEN true ELSE false END FROM agendamento a " +
            "WHERE a.aluno.id = :alunoId " +
            "  AND a.personal.id = :personalId " +
            "  AND a.data < :datafim " +
            "  AND a.dataFim > :data")
    boolean existeConflito(
            @Param("alunoId") Long alunoId,
            @Param("personalId") Long personalId,
            @Param("data") LocalDateTime data,
            @Param("datafim") LocalDateTime datafim
    );

    @Query("SELECT CASE WHEN COUNT(a) > 0 THEN true ELSE false END FROM agendamento a " +
            "WHERE a.aluno.id = :alunoId " +
            "  AND a.personal.id = :personalId " +
            "  AND a.id <> :agendamentoId " +
            "  AND a.data < :datafim " +
            "  AND a.dataFim > :data")
    boolean existeConflitoExcluindoAgendamento(
            @Param("alunoId") Long alunoId,
            @Param("personalId") Long personalId,
            @Param("data") LocalDateTime data,
            @Param("datafim") LocalDateTime datafim,
            @Param("agendamentoId") Long agendamentoId
    );

}

