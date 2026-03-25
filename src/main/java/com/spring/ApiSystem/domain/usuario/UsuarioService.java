package com.spring.ApiSystem.domain.usuario;


import com.spring.ApiSystem.domain.telefone.TelefoneService;
import com.spring.ApiSystem.domain.telefone.dto.request.ReqAtualizarTelefoneDTO;
import com.spring.ApiSystem.domain.usuario.dto.request.ReqAtualizarSenhaDto;
import com.spring.ApiSystem.domain.usuario.events.UsuarioEventPublisher;
import com.spring.ApiSystem.domain.usuario.exception.EmailExistenteException;
import com.spring.ApiSystem.domain.usuario.exception.SenhaNaoCorrespondeAtual;
import com.spring.ApiSystem.domain.usuario.exception.SenhaInvalidaException;
import com.spring.ApiSystem.domain.usuario.exception.UsuarioNaoEncontradoException;


import com.spring.ApiSystem.shared.security.ArgonService;
import com.spring.ApiSystem.shared.service.HtmlSanitizer;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;
import java.util.regex.Pattern;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final ArgonService argonService;
    private final LocalImageStorageService imageStorageService;
    private final TelefoneService telefoneService;
    private final UsuarioEventPublisher usuarioEventPublisher;
    private final HtmlSanitizer htmlSanitizer;

    public UsuarioService(UsuarioRepository usuarioRepository, ArgonService argonService, LocalImageStorageService imageStorageService, TelefoneService telefoneService, UsuarioEventPublisher usuarioEventPublisher, HtmlSanitizer htmlSanitizer) {
        this.usuarioRepository = usuarioRepository;
        this.argonService = argonService;
        this.imageStorageService = imageStorageService;
        this.telefoneService = telefoneService;
        this.usuarioEventPublisher = usuarioEventPublisher;
        this.htmlSanitizer = htmlSanitizer;
    }

    private static final Pattern SENHA_COM_MINUSCULA = Pattern.compile(".*[a-z].*");
    private static final Pattern SENHA_COM_MAIUSCULA = Pattern.compile(".*[A-Z].*");
    private static final Pattern SENHA_COM_NUMERO = Pattern.compile(".*\\d.*");
    private static final Pattern SENHA_COM_ESPECIAL = Pattern.compile(".*[^a-zA-Z0-9].*");

    public Boolean removerUsuario(String email) {
        Usuario usuario = buscarUsuarioPorEmail(email);
        usuario.setAtivo(false);
        usuarioEventPublisher.publishUsuarioRemovido(usuario);
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

    public void validarComplexidadeSenha(String senha) {
        if (senha == null || senha.isBlank()) {
            throw new SenhaInvalidaException("A senha é obrigatória");
        }
        if (senha.length() < 8) {
            throw new SenhaInvalidaException("A senha deve ter no mínimo 8 caracteres");
        }
        if (!SENHA_COM_MINUSCULA.matcher(senha).matches()) {
            throw new SenhaInvalidaException("A senha deve conter pelo menos uma letra minúscula");
        }
        if (!SENHA_COM_MAIUSCULA.matcher(senha).matches()) {
            throw new SenhaInvalidaException("A senha deve conter pelo menos uma letra maiúscula");
        }
        if (!SENHA_COM_NUMERO.matcher(senha).matches()) {
            throw new SenhaInvalidaException("A senha deve conter pelo menos um número");
        }
        if (!SENHA_COM_ESPECIAL.matcher(senha).matches()) {
            throw new SenhaInvalidaException("A senha deve conter pelo menos um caractere especial");
        }
    }

    public void atualizarSenha(ReqAtualizarSenhaDto dto, Usuario usuario) {
        validarSenhaAtual(dto.senhaAtual(), usuario);
        validarComplexidadeSenha(dto.senhaNova());
        aplicarSenhaCriptografada(usuario, dto.senhaNova());
        salvarUsuario(usuario);
    }

    public Usuario buscarUsuarioPorPaisDddNumero(String pais, String ddd, String numero) {
        return telefoneService.buscarUsuarioPorPaisDddNumero(pais, ddd, numero);
    }

    /**
     * Sanitiza os campos de texto livre de um usuário (nome e sexo).
     * Remove qualquer conteúdo HTML potencialmente perigoso.
     *
     * @param usuario O usuário a ser sanitizado
     */
    public void sanitizeUsuario(Usuario usuario) {
        if (usuario.getNome() != null) {
            usuario.setNome(htmlSanitizer.sanitize(usuario.getNome()));
        }
        if (usuario.getSexo() != null) {
            usuario.setSexo(htmlSanitizer.sanitize(usuario.getSexo()));
        }
    }

}
