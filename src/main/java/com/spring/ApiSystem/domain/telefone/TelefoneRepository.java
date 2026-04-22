package com.spring.ApiSystem.domain.telefone;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TelefoneRepository extends JpaRepository<Telefone, Long> {

    List<Telefone> findTelefoneByUsuario_Id(Long usuarioId);
    Optional<Telefone> findTelefoneByPaisAndDddAndNumero(String pais,String ddd, String numero);
}
