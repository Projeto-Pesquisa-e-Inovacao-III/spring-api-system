package com.spring.ApiSystem.usuario;

import com.spring.ApiSystem.usuario.dto.response.ResAtualizarUsuarioDTO;
import com.spring.ApiSystem.usuario.exception.EmailExistenteException;
import com.spring.ApiSystem.usuario.exception.SenhaNaoCorrespondeAtual;
import com.spring.ApiSystem.usuario.exception.UsuarioNaoEncontradoException;
import com.spring.ApiSystem.usuario.mapper.UsuarioMapper;
import com.spring.ApiSystem.aluno.Aluno;
import com.spring.ApiSystem.aluno.AlunoRepository;
import com.spring.ApiSystem.shared.security.ArgonService;
import com.spring.ApiSystem.usuario.dto.request.ReqCadastroUsuarioDTO;
import com.spring.ApiSystem.usuario.dto.request.ReqEditarUsuarioDTO;
import com.spring.ApiSystem.usuario.dto.response.ResCadastrarUsuarioDTO;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UsuarioService {

    private final UserRepository userRepository;
    private final AlunoRepository alunoRepository;
    private final UsuarioMapper usuarioMapper;
    private final ArgonService argonService;

    public UsuarioService(UserRepository userRepository, AlunoRepository alunoRepository, UsuarioMapper usuarioMapper, ArgonService argonService) {
        this.userRepository = userRepository;
        this.alunoRepository = alunoRepository;
        this.usuarioMapper = usuarioMapper;
        this.argonService = argonService;
    }

    public ResCadastrarUsuarioDTO cadastrarUsuario(ReqCadastroUsuarioDTO usuarioDTO) {

        if(!validarEmailExistente(usuarioDTO.email())){
            Aluno usuarioEntity = usuarioMapper.toEntityAluno(usuarioDTO);
            List<String> senhaCriptografada = argonService.criptografarSenha(usuarioEntity.getSenha());
            usuarioEntity.setSalt(senhaCriptografada.getFirst());
            usuarioEntity.setSenha(senhaCriptografada.getLast());
            return usuarioMapper.toDtoCadastrarUsuario(alunoRepository.save(usuarioEntity));
        }

        throw new EmailExistenteException();
    }

    public Boolean loginUsuario (String email, String senha) {

        Optional<Usuario> userOpt = userRepository.findByEmail(email);

        return userOpt.isPresent() &&
                userOpt.get().isAtivo() &&
                argonService.validarSenha(senha,userOpt.get().getSalt(), userOpt.get().getSenha());
    }

    public ResAtualizarUsuarioDTO atualizarUsuario(ReqEditarUsuarioDTO dto, String email){
        Optional<Usuario> usuarioEncontrado = userRepository.findByEmail(email);

        if(usuarioEncontrado.isPresent()){
            if(validarEmailExistente(dto.email()) &&
               !dto.email().equals(email)){
                throw new EmailExistenteException();
            }
            if(!argonService.validarSenha(dto.senha(), usuarioEncontrado.get().getSalt(),
                    usuarioEncontrado.get().getSenha())){
                throw new SenhaNaoCorrespondeAtual();
            }

            usuarioMapper.atualizarUsuarioParaEditarUsuarioDto(dto, usuarioEncontrado.get());

            if(dto.senhaNova() != null){
                List<String> senhaCriptografada = argonService.criptografarSenha(dto.senhaNova());
                usuarioEncontrado.get().setSalt(senhaCriptografada.getFirst());
                usuarioEncontrado.get().setSenha(senhaCriptografada.getLast());
            }

            userRepository.save(usuarioEncontrado.get());
            return usuarioMapper.toDtoAtualizarUsuario(usuarioEncontrado.get());
        }

        throw  new UsuarioNaoEncontradoException();
    }

    public Boolean removerUsuario(String email) {
        Optional<Usuario> usuarioEncontrado = userRepository.findByEmail(email);

        if(usuarioEncontrado.isPresent()){
            usuarioEncontrado.get().setAtivo(false);
            userRepository
                    .save(usuarioEncontrado.get());
            return true;
        }
        throw  new UsuarioNaoEncontradoException();
    }



    public Usuario buscarUsuarioPorEmail(String email) {
        if (validarEmailExistente(email)){
            return userRepository
                    .findByEmail(email)
                    .get();
        }
        throw new UsuarioNaoEncontradoException();
    }

    public Boolean validarEmailExistente(String email){
        return userRepository
                .findByEmail(email)
                .isPresent();
    }
}
