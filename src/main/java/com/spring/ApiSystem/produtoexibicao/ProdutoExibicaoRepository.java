package com.spring.ApiSystem.produtoexibicao;

import com.spring.ApiSystem.agendamento.dto.response.HorarioAgendadoProjectionDto;
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
    List<ResProdutoExibicaoDto> findByStatus(ProdutoExibicaoStatus produtoExibicaoStatus);

    @Query("SELECT new com.spring.ApiSystem.agendamento.dto.response.HorarioAgendadoProjectionDto(a.data AS dataInicio, p.tipoAula, a.status) " +
            "FROM agendamento a " +
            "LEFT JOIN a.produtoContratado pc " +
            "LEFT JOIN pc.produtoExibicao p " +
            "WHERE a.personal.id = :personalId " +
            "AND a.data BETWEEN :start AND :end " +
            "AND a.status IN (" +
            "com.spring.ApiSystem.agendamento.enums.AgendamentoStatus.PENDENTE_PERSONAL_APROVACAO, " +
            "com.spring.ApiSystem.agendamento.enums.AgendamentoStatus.PENDENTE_CLIENTE_APROVACAO, " +
            "com.spring.ApiSystem.agendamento.enums.AgendamentoStatus.APROVADO)")
    List<HorarioAgendadoProjectionDto> findAgendamentoSlotsByPersonalIdAndDataBetween(
            @Param("personalId") Long personalId,
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end
    );
}