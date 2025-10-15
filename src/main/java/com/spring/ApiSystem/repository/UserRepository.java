package com.spring.ApiSystem.repository;
import com.spring.ApiSystem.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<Usuario, Integer> {
    Optional<Usuario> findById(Long id);
    Optional<Usuario> findByEmail(String email);
}
