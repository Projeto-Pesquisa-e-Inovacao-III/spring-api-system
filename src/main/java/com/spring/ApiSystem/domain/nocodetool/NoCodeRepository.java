package com.spring.ApiSystem.domain.nocodetool;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface NoCodeRepository extends JpaRepository<NoCode, UUID> {
    NoCode findFirstByUserIdOrderByCreatedAtDesc(Long userId);

    NoCode findFirstByOrderByCreatedAtDesc();

    Page<NoCode> findAllByUserIdOrderByCreatedAtDesc(Long userId, Pageable pageable);
}
