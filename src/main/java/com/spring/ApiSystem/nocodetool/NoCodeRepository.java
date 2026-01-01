package com.spring.ApiSystem.nocodetool;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface NoCodeRepository extends JpaRepository<NoCode, UUID> {
}
