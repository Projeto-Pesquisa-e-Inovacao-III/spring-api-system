package com.spring.ApiSystem.domain.usuario.security;

import com.spring.ApiSystem.domain.aluno.Aluno;
import com.spring.ApiSystem.domain.aluno.AlunoRepository;
import com.spring.ApiSystem.domain.aluno.exception.AlunoNaoExisteException;
import com.spring.ApiSystem.domain.personal.Personal;
import com.spring.ApiSystem.domain.personal.PersonalRepository;
import com.spring.ApiSystem.domain.personal.exception.PersonalNaoExisteExcepetion;
import com.spring.ApiSystem.domain.usuario.Usuario;
import com.spring.ApiSystem.domain.usuario.UsuarioRepository;
import com.spring.ApiSystem.domain.usuario.exception.NaoAutorizadoException;
import com.spring.ApiSystem.domain.usuario.exception.UsuarioNaoEncontradoException;
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
    private final UsuarioRepository usuarioRepository;

    private final AlunoRepository alunoRepository;
    private final PersonalRepository personalRepository;

    public JpaUserDetailsService(UsuarioRepository usuarioRepository,
                                 AlunoRepository alunoRepository, PersonalRepository personalRepository) {
        this.usuarioRepository = usuarioRepository;
        this.alunoRepository = alunoRepository;
        this.personalRepository = personalRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        Usuario usuarioEncontrado = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado"));


        String role = "ROLE_" + usuarioEncontrado.getTipo().name();
        List<SimpleGrantedAuthority> authorities = List.of(new SimpleGrantedAuthority(role));

        return User.builder()
                .username(usuarioEncontrado.getEmail())
                .password(usuarioEncontrado.getSenha())
                .authorities(authorities)
                .disabled(!usuarioEncontrado.isAtivo())
                .build();
    }

    public Usuario getCurrentUser() {
        String email = getAuthenticatedEmail();
        return usuarioRepository.findByEmail(email)
                .orElseThrow(UsuarioNaoEncontradoException::new);
    }

    public Aluno getCurrentAluno() {
        String email = getAuthenticatedEmail();
        return alunoRepository.findByEmail(email)
                .orElseThrow(AlunoNaoExisteException::new);
    }

    public Personal getCurrentPersonal() {
        String email = getAuthenticatedEmail();
        return personalRepository.findByEmail(email)
                .orElseThrow(PersonalNaoExisteExcepetion::new);
    }

    private String getAuthenticatedEmail() {
        Authentication authentication = SecurityContextHolder
                .getContext()
                .getAuthentication();
        if (authentication == null || !authentication.isAuthenticated() || authentication.getPrincipal().equals("anonymousUser")) {
            throw new NaoAutorizadoException();
        }
        return authentication.getName();
    }

    public Optional<Usuario> isLogged(){
        Authentication authentication = SecurityContextHolder
                .getContext()
                .getAuthentication();

        if (authentication != null && authentication.isAuthenticated() && !authentication.getPrincipal().equals("anonymousUser")) {
            return usuarioRepository.findByEmail(authentication.getName());
        } else {
            return Optional.empty();
        }
    }
}
