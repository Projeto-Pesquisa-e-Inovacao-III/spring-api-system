package com.spring.ApiSystem.domain.historicoagendamento;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HistoricoAgendamentoRepository extends JpaRepository<HistoricoAgendamento, Long> {
}
