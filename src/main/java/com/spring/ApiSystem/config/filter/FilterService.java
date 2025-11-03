package com.spring.ApiSystem.config.filter;

import com.spring.ApiSystem.shared.security.token.TokenService;
import com.spring.ApiSystem.usuario.security.JpaUserDetailsService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Service
public class FilterService extends OncePerRequestFilter {

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
        if(cookie != null){
            String email = tokenService.subjectToken(cookie);
            UserDetails usuario = jpaUserDetailsService.loadUserByUsername(email);
            if(usuario == null){
                this.removerCookie(response);
            }
            else{
                Authentication authentication = new UsernamePasswordAuthenticationToken(usuario,
                            null, usuario.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }
        // Fazer o próximo filtro
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
