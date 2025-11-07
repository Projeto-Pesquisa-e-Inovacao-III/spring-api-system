package com.spring.ApiSystem.agendamento;

import com.spring.ApiSystem.aluno.Aluno;
import com.spring.ApiSystem.personal.Personal;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface AgendamentoRepository  extends JpaRepository<Agendamento, Long> {
    @Query("SELECT a FROM Agendamento a " +
            "LEFT JOIN FETCH a.produtoContratado pc " +
            "LEFT JOIN FETCH pc.produtoExibicao " +
            "LEFT JOIN FETCH a.endereco e " +
            "LEFT JOIN FETCH e.cep " +
            "LEFT JOIN FETCH a.personal " +
            "WHERE a.aluno = :aluno " +
            "ORDER BY a.situacao ASC, a.data ASC")
    Page<Agendamento> findByAlunoOrderByDataAsc(@Param("aluno") Aluno aluno, Pageable pageable);

    @Query("SELECT a FROM Agendamento a " +
            "LEFT JOIN FETCH a.produtoContratado pc " +
            "LEFT JOIN FETCH pc.produtoExibicao " +
            "LEFT JOIN FETCH a.endereco e " +
            "LEFT JOIN FETCH e.cep " +
            "LEFT JOIN FETCH a.personal " +
            "WHERE a.personal = :personal " +
            "ORDER BY a.situacao ASC, a.data ASC")
    Page<Agendamento> findByPersonalOrderByDataAsc(@Param("personal") Personal personal, Pageable pageable);


}
