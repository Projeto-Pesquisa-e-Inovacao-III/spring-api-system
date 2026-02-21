package com.spring.ApiSystem.shared.config.filter;

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

@Service
public class FilterService extends OncePerRequestFilter {
    private static final Logger logger = LoggerFactory.getLogger(FilterService.class);
    private final TokenService tokenService;
    private final JpaUserDetailsService jpaUserDetailsService;


    public FilterService(TokenService tokenService,
                         JpaUserDetailsService jpaUserDetailsService) {
        this.tokenService = tokenService;
        this.jpaUserDetailsService = jpaUserDetailsService;
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

        String cookie = this.recuperarCookie(request);

        if (cookie == null) {
            filterChain.doFilter(request, response);
            return;
        }

        String email;
        try {
            email = tokenService.subjectToken(cookie);
        } catch (Exception ex) {
            // Token mal formado / inválido -> remover cookie e rejeitar
            this.removerCookie(response);
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Token inválido");
            return;
        }

        if (email == null) {
            this.removerCookie(response);
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Token sem subject");
            return;
        }

        UserDetails usuario;
        try {
            usuario = jpaUserDetailsService.loadUserByUsername(email);
        } catch (UsernameNotFoundException ex) {
            this.removerCookie(response);
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Usuário não encontrado");
            return;
        }

        if (usuario == null || !usuario.isEnabled()) {
            this.removerCookie(response);
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Usuário inválido ou desabilitado");
            return;
        }
        logger.debug("Autenticando usuário: {}. Authorities: {}", usuario.getUsername(), usuario.getAuthorities());
        Authentication authentication = new UsernamePasswordAuthenticationToken(usuario,
                null, usuario.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        filterChain.doFilter(request, response);
    }

    /*
    Gera um cookie com o token gerado
     */
    public void gerarCookie(HttpServletResponse response, String email){
        String token = tokenService.gerarToken(email);

        Cookie cookie = new Cookie("jwt", token);

        cookie.setHttpOnly(true); // protege contra acesso via JavaScript
        cookie.setSecure(perfilAtivo.equals("prod")); // envia apenas em conexões HTTPS
        cookie.setPath("/");      // disponível para toda a aplicação
        cookie.setMaxAge(3600);   // duração do cookie de 1 hora (3600 segundos)

        response.addCookie(cookie);
    }

    /*
    Acessa a requisição e procura pelo cookie jwt onde estará o token
     */
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
}
