package com.spring.ApiSystem.domain.endereco;

import com.spring.ApiSystem.domain.usuario.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
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

    @Query(
    """
    SELECT e FROM endereco e JOIN FETCH e.cep WHERE e.usuario = :usuario
    """)
    List<Endereco> findByUsuario(Usuario usuario);

    Integer countByUsuarioId(Long usuarioId);
}
