package com.spring.ApiSystem.infrastructure.jpa;

import com.spring.ApiSystem.domain.repository.UsuarioRepository;
import com.spring.ApiSystem.infrastructure.entity.UsuarioEntity;

import java.util.Optional;

public interface UsuarioImplement extends UsuarioRepository {

    // O Repository é uma abstração dos metodos que o jpa irá realizar no banco de dados. Aqui ele já
    // tem ciencia que minha entidade de dominio é o UsuarioEntity.

    Optional<UsuarioEntity> findByEmail(String email);
    Optional<UsuarioEntity> findByCpf(String cpf);

}
