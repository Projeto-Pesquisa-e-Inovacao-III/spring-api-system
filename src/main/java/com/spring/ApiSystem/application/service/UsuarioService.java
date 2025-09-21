package com.spring.ApiSystem.application.service;

import com.spring.ApiSystem.interfaces.dto.CadastroUsuarioDTO;
import com.spring.ApiSystem.interfaces.dto.EditarUsuarioDTO;
import com.spring.ApiSystem.infrastructure.entity.UsuarioEntity;
import com.spring.ApiSystem.infrastructure.jpa.UsuarioImplement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioImplement usuarioRepository;

    public UsuarioEntity cadastrarUsuario (CadastroUsuarioDTO usuario) {
        return usuarioRepository.save(usuario);
    }

    public Optional<UsuarioEntity> loginUsuario (String email, String senha) {

        Optional<UsuarioEntity> userOpt = usuarioRepository.findByEmail(email);
        if (userOpt.isPresent() && userOpt.get().getSenha().equals(senha)) {
            return userOpt;
        }
        return Optional.empty();
    }

    public UsuarioEntity atualizarUsuario(Long id, EditarUsuarioDTO dto){
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
