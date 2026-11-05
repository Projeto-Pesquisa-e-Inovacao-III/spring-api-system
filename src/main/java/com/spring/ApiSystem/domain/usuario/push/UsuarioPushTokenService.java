package com.spring.ApiSystem.domain.usuario.push;

import com.spring.ApiSystem.domain.usuario.Usuario;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.regex.Pattern;

@Service
public class UsuarioPushTokenService {

    private static final Pattern EXPO_PUSH_TOKEN_PATTERN =
            Pattern.compile("^(ExponentPushToken|ExpoPushToken)\\[[^\\]]+\\]$");
    private static final Logger log = LoggerFactory.getLogger(UsuarioPushTokenService.class);

    private final UsuarioPushTokenRepository usuarioPushTokenRepository;

    public UsuarioPushTokenService(UsuarioPushTokenRepository usuarioPushTokenRepository) {
        this.usuarioPushTokenRepository = usuarioPushTokenRepository;
    }

    @Transactional
    public void registerToken(Usuario usuario, String rawPushToken) {
        log.info("Registrando token de push para o usuário: {}", usuario.getId());
        String pushToken = normalizeAndValidate(rawPushToken);

        UsuarioPushToken token = usuarioPushTokenRepository.findByPushToken(pushToken)
                .orElseGet(() -> new UsuarioPushToken(usuario, pushToken, "android"));

        token.setUsuario(usuario);
        token.setPushToken(pushToken);
        token.setPlatform("android");
        token.setAtivo(true);
        usuarioPushTokenRepository.save(token);
    }

    @Transactional
    public void removeToken(Usuario usuario, String rawPushToken) {
        String pushToken = normalizeAndValidate(rawPushToken);

        usuarioPushTokenRepository.findByPushToken(pushToken).ifPresent(existing -> {
            if (existing.getUsuario().getId().equals(usuario.getId())) {
                usuarioPushTokenRepository.delete(existing);
            }
        });
    }

    @Transactional
    public void removeAllTokensForUser(Usuario usuario) {
        if (usuario != null) {
            usuarioPushTokenRepository.deleteAllByUsuario(usuario);
        }
    }

    @Transactional(readOnly = true)
    public List<String> findActiveTokensByEmail(String email) {
        return usuarioPushTokenRepository.findAllByUsuario_EmailAndAtivoTrue(email)
                .stream()
                .map(UsuarioPushToken::getPushToken)
                .toList();
    }

    @Transactional
    public void deactivateToken(String rawPushToken) {
        String pushToken = normalizeAndValidate(rawPushToken);
        usuarioPushTokenRepository.findByPushToken(pushToken).ifPresent((token) -> {
            token.setAtivo(false);
            usuarioPushTokenRepository.save(token);
        });
    }

    private String normalizeAndValidate(String rawPushToken) {
        if (rawPushToken == null || rawPushToken.isBlank()) {
            throw new IllegalArgumentException("Push token é obrigatório");
        }

        String normalized = rawPushToken.trim();
        if (!EXPO_PUSH_TOKEN_PATTERN.matcher(normalized).matches()) {
            throw new IllegalArgumentException("Push token inválido");
        }

        return normalized;
    }
}
