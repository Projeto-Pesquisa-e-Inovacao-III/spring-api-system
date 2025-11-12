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


    public ResCadastrarUsuarioDTO cadastrarUsuario(ReqCadastroUsuarioDTO usuarioDTO) {
        validarEmailExistente(usuarioDTO.email());

        Aluno usuarioEntity = usuarioMapper.toEntityAluno(usuarioDTO);
        aplicarSenhaCriptografada(usuarioEntity, usuarioEntity.getSenha());

        return usuarioMapper.toDtoCadastrarUsuario(alunoService.cadastrarAluno(usuarioEntity));
    }

    public ResAtualizarUsuarioDTO atualizarUsuario(ReqEditarUsuarioDTO dto, String email) {
        Usuario usuario = buscarUsuarioPorEmail(email);

        validarEmailNaoEmUso(dto.email(), email);
        validarSenhaAtual(dto.senha(), usuario);

        usuarioMapper.atualizarUsuarioParaEditarUsuarioDto(dto, usuario);

        if (dto.senhaNova() != null) {
            aplicarSenhaCriptografada(usuario, dto.senhaNova());
        }

        usuarioRepository.save(usuario);
        return usuarioMapper.toDtoAtualizarUsuario(usuario);
    }


    public Boolean removerUsuario(String email) {
        Usuario usuario = buscarUsuarioPorEmail(email);
        usuario.setAtivo(false);
        usuarioRepository.save(usuario);
        return true;
    }

    public Boolean loginUsuario(String email, String senha) {
        Optional<Usuario> userOpt = buscarPorEmail(email);

        return userOpt.isPresent() &&
                userOpt.get().isAtivo() &&
                argonService.validarSenha(senha, userOpt.get().getSalt(), userOpt.get().getSenha());
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
        return usuarioRepository.existsByEmail(email);
    }

    private void validarEmailExistente(String email) {
        if (emailExiste(email)) {
            throw new EmailExistenteException();
        }
    }

    private void validarEmailNaoEmUso(String novoEmail, String emailAtual) {
        if (emailExiste(novoEmail) && !novoEmail.equals(emailAtual)) {
            throw new EmailExistenteException();
        }
    }

    private void validarSenhaAtual(String senhaInformada, Usuario usuario) {
        if (!argonService.validarSenha(senhaInformada, usuario.getSalt(), usuario.getSenha())) {
            throw new SenhaNaoCorrespondeAtual();
        }
    }

    private void aplicarSenhaCriptografada(Usuario usuario, String senhaPlain) {
        List<String> cript = argonService.criptografarSenha(senhaPlain);
        usuario.setSalt(cript.get(0));
        usuario.setSenha(cript.get(1));
    }

}
