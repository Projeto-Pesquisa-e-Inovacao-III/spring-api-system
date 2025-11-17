package com.spring.ApiSystem.usuario;

import com.spring.ApiSystem.telefone.dto.request.ReqAtualizarTelefoneDTO;
import com.spring.ApiSystem.usuario.dto.response.ResAtualizarUsuarioDTO;
import com.spring.ApiSystem.usuario.exception.EmailExistenteException;
import com.spring.ApiSystem.usuario.exception.SenhaNaoCorrespondeAtual;
import com.spring.ApiSystem.usuario.exception.UsuarioNaoEncontradoException;
import com.spring.ApiSystem.usuario.mapper.UsuarioMapper;
import com.spring.ApiSystem.shared.security.ArgonService;
import com.spring.ApiSystem.usuario.dto.request.ReqEditarUsuarioDTO;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final UsuarioMapper usuarioMapper;
    private final ArgonService argonService;
    private final LocalImageStorageService imageStorageService;

    public UsuarioService(LocalImageStorageService imageStorageService, ArgonService argonService, UsuarioMapper usuarioMapper, UsuarioRepository usuarioRepository) {
        this.imageStorageService = imageStorageService;
        this.argonService = argonService;
        this.usuarioMapper = usuarioMapper;
        this.usuarioRepository = usuarioRepository;
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

    public Usuario buscarUsuarioPorId(Integer id) {
        return usuarioRepository
                .findById(id)
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

    public void validarEmailNaoEmUso(String novoEmail, String emailAtual) {
        if (emailExiste(novoEmail) && !novoEmail.equals(emailAtual)) {
            throw new EmailExistenteException();
        }
    }

    public void validarSenhaAtual(String senhaInformada, Usuario usuario) {
        if (!argonService.validarSenha(senhaInformada, usuario.getSalt(), usuario.getSenha())) {
            throw new SenhaNaoCorrespondeAtual();
        }
    }

    public String trocarFotoUsuario(MultipartFile imagem, String fotoAtualPath) throws IOException {
        return imageStorageService.trocarImagem(imagem, Paths.get(fotoAtualPath));
    }

    public String salvarFotoUsuario(MultipartFile imagem) throws IOException {
        return imageStorageService.salvarBlob(imagem);
    }

    public Resource buscarFoto(String nomeArquivo) throws IOException {
        return imageStorageService.buscarImagem(nomeArquivo);
    }

    public void deletarFoto(String path) throws IOException {
        if (path == null || path.isBlank()) return;
        imageStorageService.deletarImagem(java.nio.file.Paths.get(path));
    }

    public void salvarUsuario(Usuario usuario) {
        usuarioRepository.save(usuario);
    }

    public void atualizarTelefones(Usuario usuario, List<ReqAtualizarTelefoneDTO> telefonesDTO) {
        for (ReqAtualizarTelefoneDTO telefoneDTO : telefonesDTO) {
            usuario.getTelefones().stream()
                    .filter(t -> t.getId().equals(telefoneDTO.id()))
                    .findFirst()
                    .ifPresent(telefone -> {
                        telefone.setDdd(telefoneDTO.ddd());
                        telefone.setNumero(telefoneDTO.numero());
                    });
        }
    }

}
