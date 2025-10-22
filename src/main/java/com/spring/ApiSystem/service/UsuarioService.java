package com.spring.ApiSystem.service;

import com.spring.ApiSystem.exception.EmailExistenteException;
import com.spring.ApiSystem.exception.SenhaNaoCorrespondeAtual;
import com.spring.ApiSystem.mapper.UsuarioMapper;
import com.spring.ApiSystem.model.Usuario;
import com.spring.ApiSystem.repository.UserRepository;
import com.spring.ApiSystem.dto.usuario.request.CadastroUsuarioDTO;
import com.spring.ApiSystem.dto.usuario.request.EditarUsuarioDTO;
import com.spring.ApiSystem.dto.usuario.response.ResUsuarioDTO;
import org.springframework.stereotype.Service;

import java.util.List;
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
        if(!validarEmailExistente(usuarioDTO.getEmail())){
            Usuario usuarioEntity = usuarioMapper.toEntity(usuarioDTO);
            List<String> senhaCriptografada = argonService.criptografarSenha(usuarioEntity.getSenha());
            usuarioEntity.setSalt(senhaCriptografada.getFirst());
            usuarioEntity.setSenha(senhaCriptografada.getLast());
            return usuarioMapper.toDto(userRepository.save(usuarioEntity));
        }
        throw new EmailExistenteException();
    }

    public List<Usuario> listar(){
        return userRepository.findAll();
    }

    public Boolean loginUsuario (String email, String senha) {
        Optional<Usuario> userOpt = userRepository.findByEmail(email);
        return userOpt.isPresent() &&
                userOpt.get().isAtivo() &&
                argonService.validarSenha(senha,userOpt.get().getSalt(), userOpt.get().getSenha());
    }

    public Usuario atualizarUsuario(EditarUsuarioDTO dto, String email){
        Optional<Usuario> usuarioEncontrado = userRepository.findByEmail(email);

        if(usuarioEncontrado.isPresent()){
            if(!validarEmailExistente(dto.getEmail()) ||
               dto.getEmail().equals(email)){
                usuarioMapper.atualizarUsuarioFromEditarUsuarioDto(dto, usuarioEncontrado.get());
                if(argonService.validarSenha(dto.getSenha(), usuarioEncontrado.get().getSalt(),
                                            usuarioEncontrado.get().getSenha()) &&
                                             dto.getSenhaNova() != null){
                    List<String> senhaCriptografada = argonService.criptografarSenha(
                            dto.getSenhaNova());
                    usuarioEncontrado.get().setSalt(senhaCriptografada.getFirst());
                    usuarioEncontrado.get().setSenha(senhaCriptografada.getLast());
                }
                else{
                    throw new SenhaNaoCorrespondeAtual();
                }

                return userRepository.save(usuarioEncontrado.get());
            }
            throw new EmailExistenteException();
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

    public Boolean validarEmailExistente(String email){
        return userRepository.findByEmail(email).isPresent();
    }
}
