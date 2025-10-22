package com.spring.ApiSystem.repository;

import com.spring.ApiSystem.model.CEP;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CepRepository extends JpaRepository<CEP, String> {
}
