package com.spring.ApiSystem.shared.infrastructure.websocket;

import com.spring.ApiSystem.domain.usuario.security.JpaUserDetailsService;
import com.spring.ApiSystem.shared.security.token.TokenService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;

@Component
public class StompAuthInterceptor implements ChannelInterceptor {

    private static final Logger logger = LoggerFactory.getLogger(StompAuthInterceptor.class);

    private final TokenService tokenService;
    private final JpaUserDetailsService jpaUserDetailsService;

    public StompAuthInterceptor(TokenService tokenService, JpaUserDetailsService jpaUserDetailsService) {
        this.tokenService = tokenService;
        this.jpaUserDetailsService = jpaUserDetailsService;
    }

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);

        if (accessor.getCommand() == StompCommand.CONNECT) {
            List<String> authorizationHeaders = accessor.getNativeHeader("Authorization");
            String rawToken = resolveToken(authorizationHeaders);

            // Fallback: check session attributes (populated by handshake interceptor if token was passed as query param)
            if ((rawToken == null || rawToken.isBlank()) && accessor.getSessionAttributes() != null) {
                Object sessionToken = accessor.getSessionAttributes().get("jwt");
                if (sessionToken instanceof String) {
                    rawToken = (String) sessionToken;
                    logger.debug("STOMP CONNECT: found token in session attributes");
                }
            }

            if (rawToken == null || rawToken.isBlank()) {
                logger.warn("Conexão STOMP rejeitada: token de autorização ausente");
                return null;
            }

            try {
                String token = rawToken.startsWith("Bearer ") ? rawToken.substring(7).trim() : rawToken.trim();
                String email = tokenService.subjectToken(token);
                Set<String> roles = tokenService.getRolesFromToken(token);

                UserDetails userDetails = jpaUserDetailsService.loadUserByUsernameAndRoles(email, roles);
                Authentication authentication = new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities()
                );

                accessor.setUser(authentication);
                SecurityContextHolder.getContext().setAuthentication(authentication);
                logger.info("STOMP CONNECT autenticado para o usuário {}", email);
            } catch (Exception ex) {
                logger.warn("Conexão STOMP rejeitada: token inválido ({})", ex.getMessage());
                return null;
            }
        }

        return message;
    }

    private String resolveToken(List<String> values) {
        if (values == null || values.isEmpty()) {
            return null;
        }

        return values.get(0);
    }
}
