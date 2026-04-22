package com.spring.ApiSystem.domain.usuario;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

@NoRepositoryBean
public interface UsuarioBaseRepository<T extends Usuario> extends JpaRepository<T, Long> {

    @Query("SELECT u FROM #{#entityName} u WHERE u.email = :email")
    Optional<T> findByEmail(@Param("email") String email);

    @Query("SELECT u FROM #{#entityName} u WHERE u.ativo = true")
    Page<T> findAllAtivos(Pageable pageable);

    @Query("SELECT u FROM #{#entityName} u LEFT JOIN FETCH u.roles WHERE u.email = :email and u.ativo = true")
    Optional<T> findByEmailWithRoles(@Param("email") String email);

}
