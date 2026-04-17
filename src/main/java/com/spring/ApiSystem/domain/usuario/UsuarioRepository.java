package com.spring.ApiSystem.domain.usuario;
import com.spring.ApiSystem.domain.aluno.Aluno;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    Optional<Usuario> findByEmail(String email);
    boolean existsByEmail(String email);

    //Tá duplicado? Sim, mas é a vida
    @Query("SELECT u FROM Usuario u LEFT JOIN FETCH u.roles WHERE u.email = :email")
    Optional<Usuario> findByEmailWithRoles(String email);

    @Query("SELECT u.ativo FROM Usuario u WHERE u.email = :email")
    boolean existsAtivoByEmail(String email);

    @EntityGraph(attributePaths = {"roles", "admin", "personal", "aluno"})
    @Query("SELECT a FROM Usuario a WHERE a.ativo = true")
    Page<Usuario> findAllAtivos(Pageable pageable);
}
