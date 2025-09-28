package com.spring.ApiSystem.service;

import com.spring.ApiSystem.dto.usuario.response.UsuarioAutenticado;
import com.spring.ApiSystem.model.User;
import com.spring.ApiSystem.repository.UserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
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
        String token = this.recuperarToken(request);
        if(token != null){
            String email = tokenService.validarToken(token);
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
    Acessa o cabeçalho da requisição e coleta o token informado
     */
    private String recuperarToken(HttpServletRequest request){
        String authHeader = request.getHeader("Authorization");
        if(authHeader == null) return null;
        return authHeader.replace("Bearer ", "");
    }
}
