package com.spring.ApiSystem.domain.personal;

import com.spring.ApiSystem.domain.usuario.Usuario;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface PersonalRepository extends JpaRepository<Personal, Long> {
    boolean existsByCref(String cref);

    @Query("SELECT p FROM Personal p JOIN FETCH p.usuario u WHERE u.ativo = true")
    Page<Personal> findAllAtivos(Pageable pageable);

    @Query("SELECT p FROM Personal p JOIN FETCH p.usuario u WHERE u.email = :email")
    Optional<Personal> findByEmail(@Param("email") String email);

    @Query("""
        SELECT p
        FROM Personal p
        JOIN FETCH p.usuario u
        LEFT JOIN FETCH u.roles
        WHERE u.email = :email
          AND u.ativo = true
    """)
    Optional<Personal> findByEmailWithRoles(@Param("email") String email);
}
