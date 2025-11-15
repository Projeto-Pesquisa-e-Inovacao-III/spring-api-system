package com.spring.ApiSystem.usuario;

import com.spring.ApiSystem.usuario.dto.response.ResAtualizarUsuarioDTO;
import com.spring.ApiSystem.usuario.exception.EmailExistenteException;
import com.spring.ApiSystem.usuario.exception.SenhaNaoCorrespondeAtual;
import com.spring.ApiSystem.usuario.exception.UsuarioNaoEncontradoException;
import com.spring.ApiSystem.usuario.mapper.UsuarioMapper;
import com.spring.ApiSystem.shared.security.ArgonService;
import com.spring.ApiSystem.usuario.dto.request.ReqEditarUsuarioDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final UsuarioMapper usuarioMapper;
    private final ArgonService argonService;

    @Value("${storage.local-dir:}")
    private String localDir;

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



    private String trocarImagem(MultipartFile imagem, Path path) throws IOException {

        deletarImagem(path);
        String newPath = salvarBlob(imagem);

        return newPath;
    }

    private String salvarBlob(MultipartFile imagem) throws IOException {
        validarImagem(imagem);
        Path path = salvarImagemLocal(imagem);

        return  path.toString();
    }

    private Path salvarImagemLocal(MultipartFile imagem) throws IOException{

        Files.createDirectories(Paths.get(localDir));

        String nomeArquivo = System.currentTimeMillis() + "_" + imagem.getOriginalFilename();

        Path caminho = Paths.get(localDir, nomeArquivo);

        Files.write(caminho, imagem.getBytes());
        return caminho;
    }

    private void deletarImagem(Path path) throws IOException {
        Files.deleteIfExists(path);
    }

    private Resource buscarImagem(String nomeArquivo) throws IOException {
        Path caminho = Paths.get(localDir, nomeArquivo);

        if (!Files.exists(caminho)) {
            throw new FileNotFoundException("Imagem não encontrada: " + nomeArquivo);
        }

        UrlResource resource = new UrlResource(caminho.toUri());

        if (resource.exists() && resource.isReadable()) {
            return resource;
        } else {
            throw new IOException("Não foi possível ler a imagem");
        }
    }


    private void validarImagem(MultipartFile imagem) {
        if (imagem == null || imagem.isEmpty()) {
            throw new IllegalArgumentException("Imagem não pode ser nula ou vazia");
        }

        String contentType = imagem.getContentType();
        if (contentType == null || !contentType.startsWith("image/")) {
            throw new IllegalArgumentException("Arquivo não é uma imagem");
        }

        if (imagem.getSize() > 5 * 1024 * 1024) {
            throw new IllegalArgumentException("Imagem muito grande (máx 5MB)");
        }

    }

}
