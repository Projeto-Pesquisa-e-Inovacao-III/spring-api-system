package com.spring.ApiSystem.infrastructure.jpa.user;

import com.spring.ApiSystem.domain.entity.Usuario;
import com.spring.ApiSystem.domain.repository.UsuarioRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public class UsuarioRepositoryImpl implements UsuarioRepository {

    private final JpaUsuarioRepository jpaUsuarioRepository;

    public UsuarioRepositoryImpl(JpaUsuarioRepository jpaUsuarioRepository) {this.jpaUsuarioRepository = jpaUsuarioRepository;}

    @Override
    public Optional<Usuario> findByEmail(String email) {
        return jpaUsuarioRepository.findByEmail(email);
    }

    @Override
    public Optional<Usuario> findByCpf(String cpf) {
        return jpaUsuarioRepository.findByCpf(cpf);
    }

    @Override
    public Usuario save(Usuario usuario) {
        return jpaUsuarioRepository.save(usuario);
    }

    @Override
    public Optional<Usuario> findById(Long id) {
        return jpaUsuarioRepository.findById(id);
    }
}
