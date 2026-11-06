package com.spring.ApiSystem.domain.notificacao;

import com.spring.ApiSystem.domain.usuario.Usuario;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Repository
public interface NotificacaoRepository extends JpaRepository<Notificacao, Long> {

    List<Notificacao> findAllByUsuarioOrderByCreatedAtDesc(Usuario usuario);

    List<Notificacao> findAllByUsuario_EmailOrderByCreatedAtDesc(String email);

    Page<Notificacao> findAllByUsuarioOrderByCreatedAtDesc(Usuario usuario, Pageable pageable);

    List<Notificacao> findAllByUsuarioAndLidaOrderByCreatedAtDesc(Usuario usuario, boolean lida);

    List<Notificacao> findAllByUsuarioAndLidaFalseOrderByCreatedAtDesc(Usuario usuario);

    Page<Notificacao> findAllByUsuarioAndLidaOrderByCreatedAtDesc(Usuario usuario, boolean lida, Pageable pageable);

    Page<Notificacao> findAllByUsuarioAndLidaFalseOrderByCreatedAtDesc(Usuario usuario, Pageable pageable);

    Optional<Notificacao> findByIdAndUsuario(Long id, Usuario usuario);

    long countByUsuarioAndLidaFalse(Usuario usuario);

    long countByUsuario_EmailAndLidaFalse(String email);

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("UPDATE Notificacao n SET n.lida = true, n.readAt = :now WHERE n.usuario = :usuario AND n.lida = false")
    int marcarTodasComoLidas(@Param("usuario") Usuario usuario, @Param("now") Instant now);
}
