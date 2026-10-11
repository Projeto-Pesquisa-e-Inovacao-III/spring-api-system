package com.spring.ApiSystem.domain.musculosecundario;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MusculoSecundarioRepository extends JpaRepository<MusculoSecundario, Long> {
    Optional<MusculoSecundario> findByName(String name);
}
