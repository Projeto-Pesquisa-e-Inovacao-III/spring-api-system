package com.spring.ApiSystem.usuario;

import com.spring.ApiSystem.usuario.dto.response.ResAtualizarUsuarioDTO;
import com.spring.ApiSystem.usuario.exception.EmailExistenteException;
import com.spring.ApiSystem.usuario.exception.SenhaNaoCorrespondeAtual;
import com.spring.ApiSystem.usuario.exception.UsuarioNaoEncontradoException;
import com.spring.ApiSystem.usuario.mapper.UsuarioMapper;
import com.spring.ApiSystem.shared.security.ArgonService;
import com.spring.ApiSystem.usuario.dto.request.ReqEditarUsuarioDTO;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final UsuarioMapper usuarioMapper;
    private final ArgonService argonService;

    public UsuarioService(UsuarioRepository usuarioRepository, UsuarioMapper usuarioMapper, ArgonService argonService) {
        this.usuarioRepository = usuarioRepository;
        this.usuarioMapper = usuarioMapper;
        this.argonService = argonService;
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
    Usuario userOpt = buscarUsuarioPorEmail(email);

        return
                userOpt.isAtivo() &&
                argonService.validarSenha(senha, userOpt.getSalt(), userOpt.getSenha());
    }

    public Usuario buscarUsuarioPorEmail(String email) {
        return usuarioRepository
                .findByEmail(email)
                .orElseThrow(UsuarioNaoEncontradoException::new);
    }

    public boolean emailExiste(String email) {
        return usuarioRepository.existsByEmail(email);
    }

    public void validarEmailExistente(String email) {
        if (emailExiste(email)) {
            throw new EmailExistenteException();
        }
    }

    public void aplicarSenhaCriptografada(Usuario usuario, String senhaPlain) {
        List<String> cript = argonService.criptografarSenha(senhaPlain);
        usuario.setSalt(cript.get(0));
        usuario.setSenha(cript.get(1));
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

}
