package com.spring.ApiSystem.repository;
import com.spring.ApiSystem.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UsuarioRepository {

    Optional<Usuario> findByEmail(String email);
    Optional<Usuario> findByCpf(String cpf);
    Usuario save(Usuario usuario);
    Optional<Usuario> findById(Long id);

}
