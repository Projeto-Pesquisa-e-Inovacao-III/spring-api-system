package com.spring.ApiSystem.domain.usuario;
import com.spring.ApiSystem.domain.aluno.Aluno;
import com.spring.ApiSystem.domain.usuario.enums.Role;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    Optional<Usuario> findByEmail(String email);
    boolean existsByEmail(String email);

    //Tá duplicado? Sim, mas é a vida
    @Query("SELECT u FROM Usuario u LEFT JOIN FETCH u.roles WHERE u.email = :email")
    Optional<Usuario> findByEmailWithRoles(String email);

    @EntityGraph(attributePaths = {"roles", "admin", "personal", "aluno"})
    @Query("SELECT u FROM Usuario u")
    Page<Usuario> findAllWithRoles(Pageable pageable);

    @EntityGraph(attributePaths = {"roles", "admin", "personal", "aluno"})
    @Query("SELECT u FROM Usuario u WHERE :role MEMBER OF u.roles")
    Page<Usuario> findAllWithRolesWhereRole(Pageable pageable, Role role);

    @Query("SELECT u.ativo FROM Usuario u WHERE u.email = :email")
    boolean existsAtivoByEmail(String email);

    @EntityGraph(attributePaths = {"roles", "admin", "personal", "aluno"})
    @Query("SELECT a FROM Usuario a WHERE a.ativo = true")
    Page<Usuario> findAllAtivos(Pageable pageable);

    // Busca paginada com filtros opcionais por nome e email
    @EntityGraph(attributePaths = {"roles", "admin", "personal", "aluno"})
    @Query("SELECT u FROM Usuario u WHERE (:nome IS NULL OR LOWER(u.nome) LIKE LOWER(CONCAT('%', :nome, '%'))) AND (:email IS NULL OR LOWER(u.email) LIKE LOWER(CONCAT('%', :email, '%')))")
    Page<Usuario> findAllWithRolesAndFilters(Pageable pageable, String nome, String email);

    // Busca paginada com filtros opcionais por nome, email e role
    @EntityGraph(attributePaths = {"roles", "admin", "personal", "aluno"})
    @Query("SELECT u FROM Usuario u WHERE (:role IS NULL OR :role MEMBER OF u.roles) AND (:nome IS NULL OR LOWER(u.nome) LIKE LOWER(CONCAT('%', :nome, '%'))) AND (:email IS NULL OR LOWER(u.email) LIKE LOWER(CONCAT('%', :email, '%')))")
    Page<Usuario> findAllWithRolesAndRoleAndFilters(Pageable pageable, Role role, String nome, String email);
}
