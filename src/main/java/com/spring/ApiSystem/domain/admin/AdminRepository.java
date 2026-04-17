package com.spring.ApiSystem.domain.admin;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AdminRepository extends JpaRepository<Admin, Long> {

    @Query("SELECT adm FROM Admin adm JOIN FETCH adm.usuario u WHERE u.email = :email")
    Optional<Admin> findByEmail(@Param("email") String email);

    @Query("""
        SELECT adm
        FROM Admin adm
        JOIN FETCH adm.usuario u
        LEFT JOIN FETCH u.roles
        WHERE u.email = :email
          AND u.ativo = true
    """)
    Optional<Admin> findByEmailWithRoles(@Param("email") String email);
}
