package com.spring.ApiSystem.service;

import com.spring.ApiSystem.dto.usuario.response.UsuarioAutenticado;
import com.spring.ApiSystem.mapper.UsuarioMapper;
import com.spring.ApiSystem.model.User;
import com.spring.ApiSystem.repository.UserRepository;
import com.spring.ApiSystem.dto.usuario.request.CadastroUsuarioDTO;
import com.spring.ApiSystem.dto.usuario.request.EditarUsuarioDTO;
import com.spring.ApiSystem.dto.usuario.response.ResUsuarioDTO;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import java.util.Optional;

@Service
public class UsuarioService {

    private final UserRepository userRepository;
    private final UsuarioMapper usuarioMapper;
    private final TokenService tokenService;

    public UsuarioService(UserRepository userRepository, UsuarioMapper usuarioMapper, TokenService tokenService) {
        this.userRepository = userRepository;
        this.usuarioMapper = usuarioMapper;
        this.tokenService = tokenService;
    }

    public ResUsuarioDTO cadastrarUsuario(CadastroUsuarioDTO usuarioDTO) {
        User userEntity = usuarioMapper.toEntity(usuarioDTO);
        return usuarioMapper.toDto(userRepository.save(userEntity));
    }

    public Boolean loginUsuario (String email, String senha) {
        Optional<User> userOpt = userRepository.findByEmailAndSenha(email, senha);
        return userOpt.isPresent();
    }

    public User atualizarUsuario(Long id, EditarUsuarioDTO dto){
        Optional<User> usuarioEncontrado = userRepository.findById(id);

        if(usuarioEncontrado.isPresent()){
            usuarioEncontrado.get().setNome(dto.getNome());
            usuarioEncontrado.get().setEmail(dto.getEmail());
            usuarioEncontrado.get().setSenha(dto.getSenha());
            usuarioEncontrado.get().setCpf(dto.getCpf());

            return userRepository.save(usuarioEncontrado.get());
        }

        return null;
    }

    public Boolean removerUsuario(Long id) {
        Optional<User> usuarioEncontrado = userRepository.findById(id);

        if(usuarioEncontrado.isPresent()){
            usuarioEncontrado.get().setAtivo(false);
            userRepository.save(usuarioEncontrado.get());
            return true;
        }
        return false;
    }

}
