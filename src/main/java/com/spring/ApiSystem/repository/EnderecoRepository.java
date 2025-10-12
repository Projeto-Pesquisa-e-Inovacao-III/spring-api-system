package com.spring.ApiSystem.repository;

import com.spring.ApiSystem.model.Endereco;
import com.spring.ApiSystem.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface EnderecoRepository extends JpaRepository<Endereco, Long> {
    List<Endereco> findByUsuario(Usuario usuario);
    Optional<Endereco> findByIdAndUsuario(Long id, Usuario usuario);
}
