package com.spring.ApiSystem.endereco;

import com.spring.ApiSystem.endereco.dto.response.ResEnderecoDTO;
import com.spring.ApiSystem.usuario.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
@Repository
public interface EnderecoRepository extends JpaRepository<Endereco, Long> {
    @Query("SELECT new com.spring.ApiSystem.endereco.dto.response.ResEnderecoDTO(" +
            "e.id, e.numero, e.complemento, e.unidade, e.tipo, e.cep" +
            ") FROM endereco e WHERE e.usuario.id = :usuarioId")
    List<ResEnderecoDTO> findByUsuario(Long usuarioId);

    Optional<Endereco> findByIdAndUsuario(Long id, Usuario usuario);
}
