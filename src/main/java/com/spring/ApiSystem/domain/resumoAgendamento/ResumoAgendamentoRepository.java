package com.spring.ApiSystem.domain.resumoAgendamento;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.domain.Pageable;
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
        ORDER BY r.id ASC
    """)
    List<ResumoAgendamento> findByAlunoIdAndPersonalId(Long alunoId, Long personalId,
                                                       Long proximoId, Pageable pageable);
}
