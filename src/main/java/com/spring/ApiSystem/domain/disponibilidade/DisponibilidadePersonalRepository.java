package com.spring.ApiSystem.domain.disponibilidade;


import com.spring.ApiSystem.domain.disponibilidade.enums.TipoHorario;
import com.spring.ApiSystem.shared.enums.DiaSemana;

import com.spring.ApiSystem.domain.personal.Personal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalTime;
import java.util.List;

@Repository
public interface DisponibilidadePersonalRepository extends JpaRepository<DisponibilidadePersonal, Long> {
    List<DisponibilidadePersonal> findByPersonalIdAndDiaSemanaAndAtivo(Long personalId, DiaSemana diaSemana,
                                                                       boolean ativo);

    List<DisponibilidadePersonal> findByPersonalIdAndDiaSemana(Long personalId, DiaSemana diaSemana);

    @Query("SELECT d FROM DisponibilidadePersonal d " +
            "WHERE d.personal.id = :personalId " +
            "AND d.diaSemana = :diaSemana " +
            "AND (:horaInicio < d.horaFim AND :horaFim > d.horaInicio) " +
            "AND (:horarioId IS NULL OR d.id != :horarioId)")

    List<DisponibilidadePersonal> encontrarConflitos(
            @Param("personalId") Long personalId,
            @Param("diaSemana") DiaSemana diaSemana,
            @Param("horaInicio") LocalTime horaInicio,
            @Param("horaFim") LocalTime horaFim,
            @Param("horarioId") Long horarioId
    );

    List<DisponibilidadePersonal> findByPersonal(Personal personal);

    List<DisponibilidadePersonal> findByPersonalIdAndTipo(Long personalId, TipoHorario tipoHorario);
}
