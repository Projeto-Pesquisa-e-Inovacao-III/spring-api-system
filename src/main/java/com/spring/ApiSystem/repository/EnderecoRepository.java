package com.spring.ApiSystem.repository;

import com.spring.ApiSystem.dto.endereco.response.ResEnderecoDTO;
import com.spring.ApiSystem.model.Endereco;
import com.spring.ApiSystem.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface EnderecoRepository extends JpaRepository<Endereco, Long> {
    @Query("SELECT new com.spring.ApiSystem.dto.endereco.response.ResEnderecoDTO(" +
            "e.id, e.numero, e.complemento, e.unidade, e.tipo, e.cep" +
            ") FROM endereco e WHERE e.usuario.id = :usuarioId")
    List<ResEnderecoDTO> findByUsuario(Long usuarioId);

    Optional<Endereco> findByIdAndUsuario(Long id, Usuario usuario);
}
