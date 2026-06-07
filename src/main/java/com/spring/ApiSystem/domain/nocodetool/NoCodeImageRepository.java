package com.spring.ApiSystem.domain.nocodetool;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface NoCodeImageRepository extends JpaRepository<NoCodeImage, UUID> {
}
