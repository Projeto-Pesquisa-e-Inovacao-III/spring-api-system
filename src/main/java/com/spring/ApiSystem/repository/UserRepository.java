package com.spring.ApiSystem.repository;
import com.spring.ApiSystem.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByCpf(String cpf);
    Optional<User> findById(Long id);
    Optional<User> findByEmail(String email);
}
