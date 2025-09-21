package com.spring.ApiSystem.application.service;

import com.spring.ApiSystem.application.mapper.UsuarioMapper;
import com.spring.ApiSystem.domain.entity.Usuario;
import com.spring.ApiSystem.domain.repository.UsuarioRepository;
import com.spring.ApiSystem.interfaces.dto.usuario.CadastroUsuarioDTO;
import com.spring.ApiSystem.interfaces.dto.usuario.EditarUsuarioDTO;
import com.spring.ApiSystem.interfaces.dto.usuario.ResUsuarioDTO;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final UsuarioMapper usuarioMapper;

    public UsuarioService(UsuarioRepository usuarioRepository, UsuarioMapper usuarioMapper) {
        this.usuarioRepository = usuarioRepository;
        this.usuarioMapper = usuarioMapper;
    }

    public ResUsuarioDTO cadastrarUsuario(CadastroUsuarioDTO usuarioDTO) {
        Usuario usuarioEntity = usuarioMapper.toEntity(usuarioDTO);
        return usuarioMapper.toDto(usuarioRepository.save(usuarioEntity));
    }

    public Optional<Usuario> loginUsuario (String email, String senha) {

        Optional<Usuario> userOpt = usuarioRepository.findByEmail(email);
        if (userOpt.isPresent() && userOpt.get().getSenha().equals(senha)) {
            return userOpt;
        }
        return Optional.empty();
    }

    public Usuario atualizarUsuario(Long id, EditarUsuarioDTO dto){
        return usuarioRepository.findById(id).map(usuario -> {

            usuario.setNome(dto.getNome());
            usuario.setEmail(dto.getEmail());
            usuario.setSenha(dto.getSenha());
            usuario.setCpf(dto.getCpf());

            return usuarioRepository.save(usuario);

        }).orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
    }

    public void removerUsuario(Long id) {
        usuarioRepository.findById(id).ifPresent(usuario -> {
            usuario.setAtivo(false);
            usuarioRepository.save(usuario);
        });
    }

}
