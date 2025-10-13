package com.spring.ApiSystem.service;

import com.spring.ApiSystem.mapper.UsuarioMapper;
import com.spring.ApiSystem.model.Usuario;
import com.spring.ApiSystem.repository.UserRepository;
import com.spring.ApiSystem.dto.usuario.request.CadastroUsuarioDTO;
import com.spring.ApiSystem.dto.usuario.request.EditarUsuarioDTO;
import com.spring.ApiSystem.dto.usuario.response.ResUsuarioDTO;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UsuarioService {

    private final UserRepository userRepository;
    private final UsuarioMapper usuarioMapper;
    private final ArgonService argonService;

    public UsuarioService(UserRepository userRepository,
                          UsuarioMapper usuarioMapper,
                          ArgonService argonService) {
        this.userRepository = userRepository;
        this.usuarioMapper = usuarioMapper;
        this.argonService = argonService;
    }

    public ResUsuarioDTO cadastrarUsuario(CadastroUsuarioDTO usuarioDTO) {
        Usuario usuarioEntity = usuarioMapper.toEntity(usuarioDTO);
        usuarioEntity.setSenha(argonService.criptografarSenha(usuarioEntity.getSenha()));
        return usuarioMapper.toDto(userRepository.save(usuarioEntity));
    }

    public Boolean loginUsuario (String email, String senha) {
        Optional<Usuario> userOpt = userRepository.findByEmail(email);
        return userOpt.isPresent() &&
                userOpt.get().isAtivo() &&
                argonService.validarSenha(senha, userOpt.get().getSenha());
    }

    public Usuario atualizarUsuario(EditarUsuarioDTO dto, String email){
        Optional<Usuario> usuarioEncontrado = userRepository.findByEmail(email);

        if(usuarioEncontrado.isPresent()){
            usuarioMapper.atualizarUsuarioFromEditarUsuarioDto(dto, usuarioEncontrado.get());
            usuarioEncontrado.get().setNome(dto.getNome());
            usuarioEncontrado.get().setEmail(dto.getEmail());
            usuarioEncontrado.get().setSenha(argonService.criptografarSenha(usuarioEncontrado.get().getSenha()));
            usuarioEncontrado.get().setCpf(dto.getCpf());

            return userRepository.save(usuarioEncontrado.get());
        }

        return null;
    }

    public Boolean removerUsuario(String email) {
        Optional<Usuario> usuarioEncontrado = userRepository.findByEmail(email);

        if(usuarioEncontrado.isPresent()){
            usuarioEncontrado.get().setAtivo(false);
            userRepository.save(usuarioEncontrado.get());
            return true;
        }
        return false;
    }

}
