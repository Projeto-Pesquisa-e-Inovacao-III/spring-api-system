package com.spring.ApiSystem.domain.exercicio;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExercicioMusculoRepository
        extends JpaRepository<ExercicioMusculo, ExercicioMusculoId> {
}
