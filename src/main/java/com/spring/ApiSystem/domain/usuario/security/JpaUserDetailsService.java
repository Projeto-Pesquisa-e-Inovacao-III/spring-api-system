package com.spring.ApiSystem.domain.usuario.security;

import com.spring.ApiSystem.domain.admin.Admin;
import com.spring.ApiSystem.domain.admin.AdminRepository;
import com.spring.ApiSystem.domain.admin.exception.AdminNaoExisteException;
import com.spring.ApiSystem.domain.aluno.Aluno;
import com.spring.ApiSystem.domain.aluno.AlunoRepository;
import com.spring.ApiSystem.domain.aluno.exception.AlunoNaoExisteException;
import com.spring.ApiSystem.domain.personal.Personal;
import com.spring.ApiSystem.domain.personal.PersonalRepository;
import com.spring.ApiSystem.domain.personal.exception.PersonalNaoExisteExcepetion;
import com.spring.ApiSystem.domain.usuario.enums.Role;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class JpaUserDetailsService implements UserDetailsService {

    private final UsuarioRepository usuarioRepository;
    private final AlunoRepository alunoRepository;
    private final PersonalRepository personalRepository;
    private final AdminRepository adminRepository;

    public JpaUserDetailsService(UsuarioRepository usuarioRepository,
                                 AlunoRepository alunoRepository, PersonalRepository personalRepository, AdminRepository adminRepository) {
        this.usuarioRepository = usuarioRepository;
        this.alunoRepository = alunoRepository;
        this.personalRepository = personalRepository;
        this.adminRepository = adminRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        Usuario usuarioEncontrado = usuarioRepository.findByEmailWithRoles(email)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado"));

        List<SimpleGrantedAuthority> authorities = new ArrayList<>();

        for (Role role : usuarioEncontrado.getRoles()) {
            authorities.add(new SimpleGrantedAuthority("ROLE_" + role.name()));
        }

        return User.builder()
                .username(usuarioEncontrado.getEmail())
                .password(usuarioEncontrado.getSenha())
                .authorities(authorities)
                .disabled(!usuarioEncontrado.isAtivo())
                .build();
    }

    public UserDetails loadUserByUsernameAndRoles(String email, Set<String> roles){
        boolean usuarioExistAndAtivo = usuarioRepository.existsAtivoByEmail(email);

        List<SimpleGrantedAuthority> authorities = roles.stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role))
                .toList();

        return User.builder()
                .username(email)
                .password("") // Parece estranho, mas a gente não usa a senha
                .authorities(authorities)
                .disabled(!usuarioExistAndAtivo)
                .build();
    }

    public Usuario getCurrentUser() {
        String email = getAuthenticatedEmail();
        return usuarioRepository.findByEmailWithRoles(email)
                .orElseThrow(UsuarioNaoEncontradoException::new);
    }

    public Aluno getCurrentAluno() {
        String email = getAuthenticatedEmail();
        return alunoRepository.findByEmailWithRoles(email)
                .orElseThrow(AlunoNaoExisteException::new);
    }

    public Personal getCurrentPersonal() {
        String email = getAuthenticatedEmail();
        return personalRepository.findByEmailWithRoles(email)
                .orElseThrow(PersonalNaoExisteExcepetion::new);
    }

    public Admin getCurrentAdmin() {
        String email = getAuthenticatedEmail();
        return adminRepository.findByEmailWithRoles(email)
                .orElseThrow(() -> new AdminNaoExisteException("Esse Admin não existe com o email: " + email));
    }


    //TODO: getCurrentAdmin

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
