package com.spring.ApiSystem.aluno;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AlunoRepository extends JpaRepository<Aluno, Long> {
    boolean existsByCpf(String cpf);
    Optional<Aluno> findByEmail(String email);
}
