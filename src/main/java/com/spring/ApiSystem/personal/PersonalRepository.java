package com.spring.ApiSystem.personal;

import com.spring.ApiSystem.usuario.UsuarioBaseRepository;

import org.springframework.stereotype.Repository;



@Repository
public interface PersonalRepository extends UsuarioBaseRepository<Personal> {
    boolean existsByCref(String cref);

}
