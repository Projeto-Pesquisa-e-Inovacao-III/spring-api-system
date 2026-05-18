package com.spring.ApiSystem.domain.resumoAgendamento;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ResumoAgendamentoRepository extends JpaRepository<ResumoAgendamento, Long> {
}
