package com.spring.ApiSystem.usuario.security;

import com.spring.ApiSystem.usuario.Usuario;
import com.spring.ApiSystem.usuario.UsuarioRepository;
import com.spring.ApiSystem.usuario.exception.NaoAutorizadoException;
import com.spring.ApiSystem.usuario.exception.UsuarioNaoEncontradoException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class JpaUserDetailsService implements UserDetailsService {
    public final UsuarioRepository usuarioRepository;

    public JpaUserDetailsService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Usuario usuarioEncontrado = usuarioRepository.findByEmail(email)
                .orElse(null);

        if(usuarioEncontrado != null){
            // Olhar no futuro quando for separado as roles dos usuários
            return new User(usuarioEncontrado.getEmail(),
                            usuarioEncontrado.getSenha(),
                            List.of(new SimpleGrantedAuthority("ROLE_USER" + usuarioEncontrado.getTipo()))
            );
        }

        return  null;
    }

    public Usuario getCurrentUser(){
        Authentication authentication = SecurityContextHolder
                .getContext()
                .getAuthentication();

        if (authentication == null || !authentication.isAuthenticated() || authentication.getPrincipal().equals("anonymousUser")) {
            throw new NaoAutorizadoException();
        }

        Optional<Usuario> optUser = usuarioRepository.findByEmail(authentication.getName());

        if(optUser.isEmpty()){
            throw new UsuarioNaoEncontradoException();
        }

        return optUser.get();
    }
}
