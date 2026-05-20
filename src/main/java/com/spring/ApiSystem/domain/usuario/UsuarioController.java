package com.spring.ApiSystem.domain.usuario;

import com.spring.ApiSystem.domain.aluno.Aluno;
import com.spring.ApiSystem.domain.aluno.AlunoService;
import com.spring.ApiSystem.domain.aluno.dto.response.ResBuscarAlunoPorIdDTO;
import com.spring.ApiSystem.domain.personal.PersonalService;
import com.spring.ApiSystem.domain.personal.dto.response.ResBuscarPersonalPorIdDTO;
import com.spring.ApiSystem.domain.usuario.dto.request.ReqAtualizarSenhaDto;
import com.spring.ApiSystem.domain.usuario.dto.request.ReqAuthDTO;
import com.spring.ApiSystem.domain.usuario.dto.request.ReqLoginUsuarioDTO;
import com.spring.ApiSystem.domain.usuario.exception.UsuarioNaoEncontradoException;
import com.spring.ApiSystem.domain.usuario.mapper.UsuarioMapper;
import com.spring.ApiSystem.domain.usuario.security.JpaUserDetailsService;
import com.spring.ApiSystem.shared.config.filter.FilterService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.Optional;

@Tag(name = "Usuários", description = "Operações relacionadas a usuários")
@RequestMapping("/api/usuarios")
@RestController
public class UsuarioController {

    private final UsuarioService usuarioService;
    private final FilterService filterService;
    private final JpaUserDetailsService userDetails;
    private final AlunoService alunoService;
    private final PersonalService personalService;
    private final UsuarioMapper usuarioMapper;

    public UsuarioController(
            UsuarioService usuarioService,
            FilterService filterService,
            JpaUserDetailsService userDetails,
            AlunoService alunoService,
            PersonalService personalService,
            UsuarioMapper usuarioMapper
    ) {
        this.usuarioService = usuarioService;
        this.filterService = filterService;
        this.userDetails = userDetails;
        this.alunoService = alunoService;
        this.personalService = personalService;
        this.usuarioMapper = usuarioMapper;
    }

    @Operation(summary = "Adiciona imagem ao perfil do usuário", description = "Endpoint para adicionar uma imagem ao perfil do usuário")
    @PostMapping("/me/imagem")
    public ResponseEntity<String> adicionarImagemPerfil(@RequestParam("imagem") MultipartFile imagem) {
        try {
            Usuario usuario = userDetails.getCurrentUser();

            String novoPath;
            if (usuario.getCaminhoFoto() != null && !usuario.getCaminhoFoto().isBlank()) {
                novoPath = usuarioService.trocarFotoUsuario(imagem, usuario.getCaminhoFoto());
            } else {
                novoPath = usuarioService.salvarFotoUsuario(imagem);
            }

            usuario.setCaminhoFoto(novoPath);
            usuarioService.salvarUsuario(usuario);

            return ResponseEntity.ok("Imagem atualizada com sucesso");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (IOException e) {
            return ResponseEntity.internalServerError().body("Erro ao salvar imagem");
        }
    }

    @Operation(summary = "Realizar login (necessário cadastro)", description = "Endpoint para o login de usuários no sistema")
    @PostMapping("/login")
    public ResponseEntity<Void> loginUsuario(
            @Valid @RequestBody ReqLoginUsuarioDTO dto,
            HttpServletResponse response
    ) {
        long startTime = usuarioService.getStartTime();
        try {
            Boolean isUsuarioEncontrado = usuarioService.loginUsuario(dto.email(), dto.senha());

            filterService.gerarCookie(response, dto.email(), isUsuarioEncontrado);

            return isUsuarioEncontrado ?
                    ResponseEntity.ok().build() :
                    ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        } finally {
            usuarioService.setEndTime(startTime, 1000, 5);
        }
    }

    @Operation(
            summary = "Excluir usuário (necessário login)",
            description = "Endpoint para a exclusão de usuários no sistema, onde o atributo 'ativo' define o seus status"
    )
    @PatchMapping
    public ResponseEntity<Void> deletarUsuario(
            @AuthenticationPrincipal UserDetails userDetails,
            HttpServletResponse response
    ) {
        String email = userDetails.getUsername();
        usuarioService.removerUsuario(email);

        filterService.removerCookie(response);
        return ResponseEntity.noContent().build();
    }





    @Operation(summary = "Atualizar senha", description = "Endpoint para atualizar senha do usuário logado")
    @PatchMapping("/me/alterar-senha")
    public ResponseEntity<Void> atualizarSenha(@Valid @RequestBody ReqAtualizarSenhaDto dto) {
        Usuario usuario = userDetails.getCurrentUser();
        usuarioService.atualizarSenha(dto, usuario);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Realizar logout", description = "Endpoint para o logout de usuários no sistema")
    @GetMapping("/logout")
    public ResponseEntity<Void> logoutUsuario(HttpServletResponse response) {
        filterService.removerCookie(response);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Buscar informações de usuario", description = "Endpoint para a busca as informações do usuario")
    @GetMapping("/me")
    public ResponseEntity<Object> buscarEu() {
        Usuario usuario = userDetails.getCurrentUser();

        if (usuario.isAluno()) {
            ResBuscarAlunoPorIdDTO resUsuario = alunoService.buscarAlunoPorId(usuario.getId());
            return ResponseEntity.ok(resUsuario);
        } else if (usuario.isPersonal()) {
            ResBuscarPersonalPorIdDTO resUsuario = personalService.buscarPersonalPorId(usuario.getId());
            return ResponseEntity.ok(resUsuario);
        }

        throw new UsuarioNaoEncontradoException();
    }

    @Operation(summary = "Buscar imagem do perfil do usuário", description = "Endpoint para buscar a imagem do perfil do usuário")
    @GetMapping("/me/imagem")
    public ResponseEntity<Resource> buscarImagemPerfil() {
        try {
            Usuario usuario = userDetails.getCurrentUser();

            if (usuario.getCaminhoFoto() == null || usuario.getCaminhoFoto().isBlank()) {
                return ResponseEntity.notFound().build();
            }

//          String nomeArquivo = Paths.get(usuario.getCaminhoFoto()).getFileName().toString();
            Resource resource = usuarioService.buscarFoto(usuario.getCaminhoFoto());

            return ResponseEntity.ok()
                    .header("Content-Type", "image/*")
                    .header("Cache-Control", "no-cache, no-store, must-revalidate")
                    .header("Pragma", "no-cache")
                    .header("Expires", "0")
                    .body(resource);
        } catch (IOException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Deletar imagem do perfil do usuário", description = "Endpoint para deletar a imagem do perfil do usuário")
    @DeleteMapping("/me/imagem")
    public ResponseEntity<String> deletarImagemPerfil() {
        try {
            Usuario usuario = userDetails.getCurrentUser();

            if (usuario.getCaminhoFoto() == null || usuario.getCaminhoFoto().isBlank()) {
                return ResponseEntity.notFound().build();
            }

            usuarioService.deletarFoto(usuario.getCaminhoFoto());
            usuario.setCaminhoFoto(null);
            usuarioService.salvarUsuario(usuario);

            return ResponseEntity.ok("Imagem deletada com sucesso");
        } catch (IOException e) {
            return ResponseEntity.internalServerError().body("Erro ao deletar imagem");
        }
    }

    @Operation(summary = "Buscar foto por nome", description = "Endpoint para buscar a foto do usuário por nome")
    @GetMapping("/foto/{nomeArquivo}")
    public ResponseEntity<Resource> buscarFotoPorNome(@PathVariable String nomeArquivo) {
        try {
            Resource resource = usuarioService.buscarFoto(nomeArquivo);
            return ResponseEntity.ok()
                    .header("Content-Type", "image/*")
                    .body(resource);
        } catch (IOException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Buscar informação de usuário autenticado", description = "Endpoint para buscar informação de usuário autenticado")
    @GetMapping("/auth")
    public ResponseEntity<ReqAuthDTO> auth() {
        Optional<Usuario> usuarioOpt = userDetails.isLogged();

        if (usuarioOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ReqAuthDTO(false, null, false));
        }

        Usuario usuario = usuarioOpt.get();

        if (usuario.isAluno()) {
            Aluno aluno = alunoService.buscarPorId(usuario.getId());
            return ResponseEntity.ok(
                    new ReqAuthDTO(true, usuarioMapper.toDtoAuthUser(usuario), aluno.getAtivoAnamnese())
            );
        }

        return ResponseEntity.ok(new ReqAuthDTO(true, usuarioMapper.toDtoAuthUser(usuario), null));
    }
}