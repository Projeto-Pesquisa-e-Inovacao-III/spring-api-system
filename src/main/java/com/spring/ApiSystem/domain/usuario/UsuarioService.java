package com.spring.ApiSystem.domain.usuario;


import com.spring.ApiSystem.domain.telefone.TelefoneService;
import com.spring.ApiSystem.domain.telefone.dto.request.ReqAtualizarTelefoneDTO;
import com.spring.ApiSystem.domain.usuario.dto.request.ReqAtualizarSenhaDto;
import com.spring.ApiSystem.domain.usuario.enums.Role;
import com.spring.ApiSystem.domain.usuario.events.UsuarioEventPublisher;
import com.spring.ApiSystem.domain.usuario.exception.EmailExistenteException;
import com.spring.ApiSystem.domain.usuario.exception.SenhaNaoCorrespondeAtual;
import com.spring.ApiSystem.domain.usuario.exception.UsuarioNaoEncontradoException;


import com.spring.ApiSystem.shared.security.ArgonService;
import jakarta.transaction.Transactional;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.LockSupport;
import java.util.Optional;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final ArgonService argonService;
    private final LocalImageStorageService imageStorageService;
    private final TelefoneService telefoneService;
    private final UsuarioEventPublisher usuarioEventPublisher;

    private final String DUMMY_SALT;
    private final String DUMMY_HASH;
    private final Usuario DUMMY_USUARIO;

    public UsuarioService(UsuarioRepository usuarioRepository, ArgonService argonService, LocalImageStorageService imageStorageService, TelefoneService telefoneService, UsuarioEventPublisher usuarioEventPublisher) {
        this.usuarioRepository = usuarioRepository;
        this.argonService = argonService;
        this.imageStorageService = imageStorageService;
        this.telefoneService = telefoneService;
        this.usuarioEventPublisher = usuarioEventPublisher;

        List<String> dummyArgon = argonService.criptografarSenha("dummy_senha");
        this.DUMMY_SALT  = dummyArgon.get(0);
        this.DUMMY_HASH = dummyArgon.get(1);
        this.DUMMY_USUARIO = new Usuario(DUMMY_SALT, DUMMY_HASH, true);
    }

    public Boolean removerUsuario(String email) {
        Usuario usuario = buscarUsuarioPorEmail(email);
        usuario.setAtivo(false);
        usuarioEventPublisher.publishUsuarioRemovido(usuario);
        usuarioRepository.save(usuario);

        return true;
    }

    public long getStartTime(){
        return System.nanoTime();
    }

    public void setEndTime(long startTime, int milliseconds, int millisecondsToAdd){
        long timeTarget = TimeUnit.MILLISECONDS.toNanos(milliseconds);
        long stepNanos = TimeUnit.MILLISECONDS.toNanos(millisecondsToAdd);
        long timeSpent = System.nanoTime() - startTime;

        if (timeSpent > timeTarget) {
            long excess = timeSpent - timeTarget;
            long steps = (excess + stepNanos - 1) / stepNanos;
            timeTarget += steps * stepNanos;
        }

        long timeLeft = timeTarget - timeSpent;
        if (timeLeft > 0) {
            LockSupport.parkNanos(timeLeft);
        }
    }

    public Boolean loginUsuario(String email, String senha) {
        Usuario userOpt = usuarioRepository.findByEmail(email)
                .orElse(DUMMY_USUARIO);

        return
                argonService.validarSenha(senha, userOpt.getSalt(), userOpt.getSenha()) &&
                userOpt.isAtivo();
    }

    public Usuario buscarUsuarioPorEmail(String email) {
        return usuarioRepository
                .findByEmail(email)
                .orElseThrow(UsuarioNaoEncontradoException::new);
    }

    public Optional<Usuario> getOpitionalUsuarioByEmailWithRoles(String email) {
        return usuarioRepository.findByEmailWithRoles(email);

    }

    public Usuario buscarUsuarioPorId(Long id) {
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

    public Usuario salvarUsuario(Usuario usuario) {
        return usuarioRepository.save(usuario);
    }

    public void atualizarTelefones(Usuario usuario, List<ReqAtualizarTelefoneDTO> telefonesDTO) {
        for (ReqAtualizarTelefoneDTO telefoneDTO : telefonesDTO) {
            usuario.getTelefones().stream()
                    .filter(t -> t.getId().equals(telefoneDTO.id()))
                    .findFirst()
                    .ifPresent(telefone -> {
                        telefone.setPais("55+");
                        telefone.setDdd(telefoneDTO.ddd());
                        telefone.setNumero(telefoneDTO.numero());
                    });
        }
    }

    public void atualizarSenha(ReqAtualizarSenhaDto dto, Usuario usuario) {
        validarSenhaAtual(dto.senhaAtual(), usuario);
        aplicarSenhaCriptografada(usuario, dto.senhaNova());
        salvarUsuario(usuario);
    }

    public Usuario buscarUsuarioPorPaisDddNumero(String pais, String ddd, String numero) {
        return telefoneService.buscarUsuarioPorPaisDddNumero(pais, ddd, numero);
    }

    @Transactional
    public Usuario addRoleToUsuario(Usuario usuario, Role role){
        usuario.addRole(role);
        return usuarioRepository.save(usuario);
    }

    @Transactional
    public Usuario addRoleToUsuarioById(Long id, Role role) {
        Usuario usuario = buscarUsuarioPorId(id);
        return addRoleToUsuario(usuario, role);
    }

    @Transactional
    public Usuario removeRoleFromUsuario(Usuario usuario, Role role){
        usuario.removeRole(role);
        return usuarioRepository.save(usuario);
    }

    @Transactional
    public Usuario removeRoleFromUsuarioById(Long id, Role role) {
        Usuario usuario = buscarUsuarioPorId(id);
        return removeRoleFromUsuario(usuario, role);
    }

    public Page<Usuario> findAllUsersPagedWithRoles(Pageable pageable) {
        return usuarioRepository.findAllWithRoles(pageable);
    }

    public Page<Usuario> findAllUsersPagedWithRolesAndFilters(Pageable pageable, String nome, String email) {
        return usuarioRepository.findAllWithRolesAndFilters(pageable, nome, email);
    }

    public Page<Usuario> findAllUsersPagedWithRolesAndRoleAndFilters(Pageable pageable, Role role, String nome, String email) {
        return usuarioRepository.findAllWithRolesAndRoleAndFilters(pageable, role, nome, email);
    }
}
