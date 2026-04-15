package com.spring.ApiSystem.domain.usuario;

import org.springframework.data.domain.Page;
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

    @Query("""
    SELECT u FROM #{#entityName} u
    WHERE u.ativo = true
    AND (
      :nome IS NULL OR
      TRANSLATE(LOWER(u.nome), 'áàâãäéèêëíìîïóòôõöúùûüç', 'aaaaaeeeeiiiiooooouuuuc')
      LIKE CONCAT('%',
          TRANSLATE(LOWER(:nome), 'áàâãäéèêëíìîïóòôõöúùûüç', 'aaaaaeeeeiiiiooooouuuuc'),
          '%')
    )
    """)
    Page<T> findAllAtivosContainingNome(Pageable pageable, @Param("nome") String nome);
}
