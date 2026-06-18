package com.spring.ApiSystem.domain.nocodetool;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface NoCodeImageRepository extends JpaRepository<NoCodeImage, UUID> {
    List<NoCodeImage> findByNoCode(NoCode noCode);
    void deleteByNoCode(NoCode noCode);
}
