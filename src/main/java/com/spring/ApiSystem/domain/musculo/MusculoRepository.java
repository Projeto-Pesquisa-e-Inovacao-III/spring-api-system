package com.spring.ApiSystem.domain.musculo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MusculoRepository extends JpaRepository<Musculo, Long> {
    Optional<Musculo> findByName(String name);
}
