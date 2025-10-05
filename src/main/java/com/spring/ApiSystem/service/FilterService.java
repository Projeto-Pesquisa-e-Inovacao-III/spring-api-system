package com.spring.ApiSystem.service;

import com.spring.ApiSystem.model.User;
import com.spring.ApiSystem.repository.UserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Service
public class FilterService extends OncePerRequestFilter {

    private final TokenService tokenService;
    private final UserRepository userRepository;

    public FilterService(TokenService tokenService, UserRepository userRepository) {
        this.tokenService = tokenService;
        this.userRepository = userRepository;
    }

    /*
    Função executada a cada requisição que pega o token informado
    e valida
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String cookie = this.recuperarCookie(request);
        if(cookie != null){
            String email = tokenService.validarToken(cookie);
            List<SimpleGrantedAuthority> rolePadrao = null;
            User usuarioEncontrado = null;
            if(userRepository.findByEmail(email).isPresent()){
                usuarioEncontrado = userRepository.findByEmail(email).get();
                rolePadrao = List.of(new SimpleGrantedAuthority("ROLE_USER"));
            }

            var authentication = new UsernamePasswordAuthenticationToken(usuarioEncontrado, null, rolePadrao);
            SecurityContextHolder.getContext().setAuthentication(authentication);
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
        cookie.setSecure(true);   // só envia em HTTPS
        cookie.setPath("/");      // disponível para toda a aplicação
        cookie.setMaxAge(3600);   // duração do cookie de 1 hora (3600 segundos)

        response.addCookie(cookie);
    }

    /*
    Acessa a requisição e procura pelo cookie jwt onde estará o token
     */
    public String recuperarCookie(HttpServletRequest request){
        for (Cookie cookie : request.getCookies()) {
            if(cookie.getName().equals("jwt")){
                return cookie.getValue();
            }
        }

        return null;
    }
}
