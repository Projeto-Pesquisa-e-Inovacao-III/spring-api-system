package com.spring.ApiSystem.repository;

import com.spring.ApiSystem.model.Endereco;
import com.spring.ApiSystem.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface EnderecoRepository extends JpaRepository<Endereco, Long> {
    List<Endereco> findByUsuario(User usuario);
    Optional<Endereco> findByIdAndUsuario(Long id, User usuario);
}
