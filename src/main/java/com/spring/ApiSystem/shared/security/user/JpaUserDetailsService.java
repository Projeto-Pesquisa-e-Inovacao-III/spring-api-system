package com.spring.ApiSystem.shared.security.user;

import com.spring.ApiSystem.usuario.Usuario;
import com.spring.ApiSystem.usuario.UserRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class JpaUserDetailsService implements UserDetailsService {
    public final UserRepository userRepository;

    public JpaUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Usuario usuarioEncontrado = userRepository.findByEmail(email)
                .orElse(null);

        // Olhar no futuro quando for separado as roles dos usuários
        return new User(usuarioEncontrado.getEmail(),
                        usuarioEncontrado.getSenha(),
                        List.of(new SimpleGrantedAuthority("ROLE_USER")));
    }
}
