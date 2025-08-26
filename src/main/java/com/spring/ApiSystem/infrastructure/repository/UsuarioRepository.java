package com.spring.ApiSystem.infrastructure.repository;

import com.spring.ApiSystem.domain.entity.UsuarioEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<UsuarioEntity, Long> {

    // O Repository é uma abstração dos metodos que o jpa irá realizar no banco de dados. Aqui ele já
    // tem ciencia que minha entidade de dominio é o UsuarioEntity.

    Optional<UsuarioEntity> findByEmail(String email);
    Optional<UsuarioEntity> findByCpf(Long cpf);

}
