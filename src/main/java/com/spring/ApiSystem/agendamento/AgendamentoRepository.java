package com.spring.ApiSystem.agendamento;

import com.spring.ApiSystem.agendamento.dto.response.ResAgendamentoPersonalOverviewDTO;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface AgendamentoRepository  extends JpaRepository<Agendamento, Long> {

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

    boolean existsByAlunoIdAndPersonalIdAndDataBetween(
            Long alunoId,
            Long personalId,
            LocalDateTime dataInicio,
            LocalDateTime dataFim
    );

}

    //SELECTS QUE EU PRECISO
    //BUSCAR AGENDAMENTOS POR  MAIS RECENTE E APROVADOS por aluno overview
    //BUSCAR AGENDAMENTOS POR  MAIS RECENTE E APROVADOS por personal overview
    //BUSCAR AGENDAMENTOS POR PERSONAL ORGANIZAR POR CATEGORIA E DEPOIS POR DATA solicitações


    //BUSCAR se a agendamento naquele dia e horario
    //SALVAR UM PRODUTO CONTRATADO AO CRIAR UM AGENDAMENTO(LOGICA)
    //CANCELAR UM AGENDAMENTO E RETORNA SALDO DE PRODUTOS CONTRATADOS
    //ATUALIZAR UM AGENDAMENTO, MAS SEM MUDAR PRODUTO CONTRATADO
    //BAGULHO DE QUANDO VIRAR O DIA VER OS AGENDAMENTOS QUE PASSRAM, SE VIROU O DIA JA VIRE STATUS PARA PENDENTE CONCLUIR

