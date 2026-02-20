package com.spring.ApiSystem.endereco;

import com.spring.ApiSystem.usuario.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
@Repository
public interface EnderecoRepository extends JpaRepository<Endereco, Long> {
    List<Endereco> findByUsuarioId(Long usuarioId);
    Optional<Endereco> findByIdAndUsuario(Long id, Usuario usuario);

    Optional<Endereco> findByCepIdAndNumeroAndComplementoAndUnidadeAndTipo(
            String id, String numero, String complemento, String unidade, String tipo
    );
}
