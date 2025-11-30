package com.spring.ApiSystem.personal;

import com.spring.ApiSystem.aluno.Aluno;
import com.spring.ApiSystem.usuario.UsuarioBaseRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PersonalRepository extends UsuarioBaseRepository<Personal> {
    boolean existsByCref(String cref);

    List<Aluno> findAllBy(Pageable pageable);

}
