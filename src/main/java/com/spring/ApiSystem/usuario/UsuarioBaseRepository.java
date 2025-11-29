package com.spring.ApiSystem.usuario;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

@NoRepositoryBean
public interface UsuarioBaseRepository<T extends Usuario> extends JpaRepository<T, Long> {

    @Query("SELECT a FROM #{#entityName} a WHERE a.email = :email")
    Optional<T> findByEmail(@Param("email") String email);

    @Query("SELECT u FROM #{#entityName} u WHERE u.ativo = true")
    List<T> findAllAtivos(Pageable pageable);

    @Query("SELECT u FROM #{#entityName} u WHERE u.nome LIKE %:nome%")
    List<T> findByNomeContaining(@Param("nome") String nome);
}
