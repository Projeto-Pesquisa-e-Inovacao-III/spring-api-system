package com.spring.ApiSystem.domain.usuario.push;

import com.spring.ApiSystem.domain.usuario.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UsuarioPushTokenRepository extends JpaRepository<UsuarioPushToken, Long> {
    Optional<UsuarioPushToken> findByPushToken(String pushToken);
    List<UsuarioPushToken> findAllByUsuario_EmailAndAtivoTrue(String email);
    void deleteAllByUsuario(Usuario usuario);
}
