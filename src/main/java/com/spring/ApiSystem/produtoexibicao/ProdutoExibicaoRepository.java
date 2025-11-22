package com.spring.ApiSystem.produtoexibicao;


import com.spring.ApiSystem.agendamento.dto.response.HorarioAgendadoProjectionDto;
import com.spring.ApiSystem.produtoexibicao.dto.response.ResProdutoExibicaoDTO;
import com.spring.ApiSystem.produtoexibicao.enums.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ProdutoExibicaoRepository extends JpaRepository<ProdutoExibicao, Long> {
    List<ResProdutoExibicaoDTO> findByStatus(Status status);
    List<ResProdutoExibicaoDTO> findAllBy();

    @Query("SELECT new com.spring.ApiSystem.agendamento.dto.response.HorarioAgendadoProjectionDto(a.data, p.tipoAula, a.situacao) " +
            "FROM agendamento a " +
            "LEFT JOIN a.produtoContratado pc " +
            "LEFT JOIN pc.produtoExibicao p " +
            "WHERE a.personal.id = :personalId " +
            "AND a.data BETWEEN :start AND :end " +
            "AND a.situacao IN (" +
            "com.spring.ApiSystem.agendamento.enums.Situacao.PENDENTE_PERSONAL, " +
            "com.spring.ApiSystem.agendamento.enums.Situacao.PENDENTE_CLIENTE, " +
            "com.spring.ApiSystem.agendamento.enums.Situacao.ACEITO)")
    List<HorarioAgendadoProjectionDto> findAgendamentoSlotsByPersonalIdAndDataBetween(
            @Param("personalId") Long personalId,
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end
    );
}