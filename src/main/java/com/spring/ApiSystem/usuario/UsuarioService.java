package com.spring.ApiSystem.usuario;

import com.spring.ApiSystem.aluno.AlunoService;
import com.spring.ApiSystem.usuario.dto.response.ResAtualizarUsuarioDTO;
import com.spring.ApiSystem.usuario.exception.EmailExistenteException;
import com.spring.ApiSystem.usuario.exception.SenhaNaoCorrespondeAtual;
import com.spring.ApiSystem.usuario.exception.UsuarioNaoEncontradoException;
import com.spring.ApiSystem.usuario.mapper.UsuarioMapper;
import com.spring.ApiSystem.aluno.Aluno;
import com.spring.ApiSystem.shared.security.ArgonService;
import com.spring.ApiSystem.usuario.dto.request.ReqCadastroUsuarioDTO;
import com.spring.ApiSystem.usuario.dto.request.ReqEditarUsuarioDTO;
import com.spring.ApiSystem.usuario.dto.response.ResCadastrarUsuarioDTO;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final AlunoService alunoService;
    private final UsuarioMapper usuarioMapper;
    private final ArgonService argonService;

    public UsuarioService(UsuarioRepository usuarioRepository, AlunoService alunoService, UsuarioMapper usuarioMapper, ArgonService argonService) {
        this.usuarioRepository = usuarioRepository;
        this.alunoService = alunoService;
        this.usuarioMapper = usuarioMapper;
        this.argonService = argonService;
    }

    public Boolean loginUsuario(String email, String senha) {
        Optional<Usuario> userOpt = usuarioRepository.findByEmail(email);

        return userOpt.isPresent() &&
                userOpt.get().isAtivo() &&
                argonService.validarSenha(senha, userOpt.get().getSalt(), userOpt.get().getSenha());
    }


    public ResCadastrarUsuarioDTO cadastrarUsuario(ReqCadastroUsuarioDTO usuarioDTO) {
        validarEmailExistente(usuarioDTO.email());

        Aluno usuarioEntity = usuarioMapper.toEntityAluno(usuarioDTO);
        aplicarSenhaCriptografada(usuarioEntity, usuarioEntity.getSenha());

        return usuarioMapper.toDtoCadastrarUsuario(alunoService.cadastrarAluno(usuarioEntity));
    }

    public ResAtualizarUsuarioDTO atualizarUsuario(ReqEditarUsuarioDTO dto, String email) {
        Optional<Usuario> usuarioEncontrado = usuarioRepository.findByEmail(email);

        if (usuarioEncontrado.isPresent()) {

            if (emailExiste(dto.email()) && !dto.email().equals(email)) {
                throw new EmailExistenteException();
            }

            if (!argonService.validarSenha(dto.senha(), usuarioEncontrado.get().getSalt(),
                    usuarioEncontrado.get().getSenha())) {
                throw new SenhaNaoCorrespondeAtual();
            }

            usuarioMapper.atualizarUsuarioParaEditarUsuarioDto(dto, usuarioEncontrado.get());

            if (dto.senhaNova() != null) {
                aplicarSenhaCriptografada(usuarioEncontrado.get(), dto.senhaNova());
            }

            usuarioRepository.save(usuarioEncontrado.get());
            return usuarioMapper.toDtoAtualizarUsuario(usuarioEncontrado.get());
        }

        throw new UsuarioNaoEncontradoException();
    }

    public Boolean removerUsuario(String email) {
        Optional<Usuario> usuarioEncontrado = usuarioRepository.findByEmail(email);

        if (usuarioEncontrado.isPresent()) {
            usuarioEncontrado.get().setAtivo(false);
            usuarioRepository.save(usuarioEncontrado.get());
            return true;
        }
        throw new UsuarioNaoEncontradoException();
    }

    public Usuario buscarUsuarioPorEmail(String email) {
        return usuarioRepository
                .findByEmail(email)
                .orElseThrow(UsuarioNaoEncontradoException::new);
    }

    public Optional<Usuario> buscarPorEmail(String email) {
        return usuarioRepository.findByEmail(email);
    }

    public boolean emailExiste(String email) {
        return buscarPorEmail(email).isPresent();
    }

    public void validarEmailExistente(String email) {
        if (usuarioRepository.existsByEmail(email)) {
            throw new EmailExistenteException();
        }
    }

    private void aplicarSenhaCriptografada(Usuario usuario, String senhaPlain) {
        List<String> cript = argonService.criptografarSenha(senhaPlain);
        usuario.setSalt(cript.get(0));
        usuario.setSenha(cript.get(1));
    }

}
