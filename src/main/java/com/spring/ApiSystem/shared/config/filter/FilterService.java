package com.spring.ApiSystem.shared.config.filter;

import com.spring.ApiSystem.domain.usuario.Usuario;
import com.spring.ApiSystem.domain.usuario.UsuarioService;
import com.spring.ApiSystem.domain.usuario.security.JpaUserDetailsService;
import com.spring.ApiSystem.shared.security.token.TokenService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Set;

@Service
public class FilterService extends OncePerRequestFilter {
    private static final Logger logger = LoggerFactory.getLogger(FilterService.class);
    private final TokenService tokenService;
    private final JpaUserDetailsService jpaUserDetailsService;
    private final UsuarioService usuarioService;


    public FilterService(TokenService tokenService,
                         JpaUserDetailsService jpaUserDetailsService, UsuarioService usuarioService) {
        this.tokenService = tokenService;
        this.jpaUserDetailsService = jpaUserDetailsService;
        this.usuarioService = usuarioService;
    }

    @Value("${spring.profiles.active}")
    private String perfilAtivo;

    /*
    Função executada a cada requisição que pega o token informado
    e valida
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String token = recuperarCookie(request);

        if (token == null) {
            filterChain.doFilter(request, response);
            return;
        }

        try {
            String email = tokenService.subjectToken(token);
            Set<String> roles = tokenService.getRolesFromToken(token);
            if (email != null) {
                autenticarUsuario(email, roles);
            }
        } catch (Exception ex) {
            logger.debug("Token inválido ou expirado: {}", ex.getMessage());
            removerCookie(response);
        }

        filterChain.doFilter(request, response);
    }

    /*
    Gera um cookie com o token gerado
     */
    public void gerarCookie(HttpServletResponse response, String email, boolean enviarCookie){
        Usuario usuario = usuarioService.getOpitionalUsuarioByEmailWithRoles(email)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado"));

        String token = tokenService.gerarToken(email, usuario.getRoles());

        Cookie cookie = new Cookie("jwt", token);

        cookie.setHttpOnly(true); // protege contra acesso via JavaScript
        cookie.setSecure(false); // envia apenas em conexões HTTPS
        cookie.setPath("/");      // disponível para toda a aplicação
        cookie.setMaxAge(3600);   // duração do cookie de 1 hora (3600 segundos)

        if(enviarCookie){
            response.addCookie(cookie);
        }
    }


    public String recuperarCookie(HttpServletRequest request){
        if(request.getCookies() != null){
            for (Cookie cookie : request.getCookies()) {
                if(cookie.getName().equals("jwt")){
                    return cookie.getValue();
                }
            }
        }

        return null;
    }


    public void removerCookie(HttpServletResponse response){
        Cookie remover = new Cookie("jwt", null);
        remover.setPath("/");
        remover.setMaxAge(0);
        remover.setHttpOnly(true);
        response.addCookie(remover);
    }

    private void autenticarUsuario(String email, Set<String> roles) {
        if (SecurityContextHolder.getContext().getAuthentication() != null) {
            return;
        }

        try {
            UserDetails usuario = jpaUserDetailsService.loadUserByUsernameAndRoles(email, roles);

            if (usuario == null || !usuario.isEnabled()) {
                return;
            }
            Authentication authentication =
                    new UsernamePasswordAuthenticationToken(
                            usuario,
                            null,
                            usuario.getAuthorities()
                    );
            SecurityContextHolder.getContext().setAuthentication(authentication);

            logger.debug("Usuário autenticado: {}", usuario.getUsername());

        } catch (UsernameNotFoundException ex) {
            logger.debug("Usuário do token não encontrado");
        }
    }
}


