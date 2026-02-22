package com.spring.ApiSystem.domain.personal;

import com.spring.ApiSystem.domain.usuario.UsuarioBaseRepository;

import org.springframework.stereotype.Repository;



@Repository
public interface PersonalRepository extends UsuarioBaseRepository<Personal> {
    boolean existsByCref(String cref);

}
