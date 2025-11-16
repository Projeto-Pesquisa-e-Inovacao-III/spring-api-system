package com.spring.ApiSystem.usuario;

import com.spring.ApiSystem.aluno.AlunoService;
import com.spring.ApiSystem.aluno.dto.response.ResBuscarAlunoPorIdDTO;
import com.spring.ApiSystem.personal.PersonalService;
import com.spring.ApiSystem.personal.dto.response.ResBuscarPersonalPorIdDTO;
import com.spring.ApiSystem.usuario.dto.request.ReqLoginUsuarioDTO;
import com.spring.ApiSystem.usuario.dto.request.ReqEditarUsuarioDTO;
import com.spring.ApiSystem.config.filter.FilterService;
import com.spring.ApiSystem.usuario.dto.response.ResAtualizarUsuarioDTO;
import com.spring.ApiSystem.usuario.enums.TipoUsuario;
import com.spring.ApiSystem.usuario.exception.UsuarioNaoEncontradoException;
import com.spring.ApiSystem.usuario.security.JpaUserDetailsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Paths;

@Tag(name = "Usuários", description = "Operações relacionadas a usuários")
@RequestMapping("/usuarios")
@RestController
public class UsuarioController {

    private final UsuarioService usuarioService;
    private final FilterService filterService;
    private final JpaUserDetailsService userDetails;
    private final AlunoService alunoService;
    private final PersonalService personalService;

    public UsuarioController(UsuarioService usuarioService,
                             FilterService filterService, JpaUserDetailsService userDetails, AlunoService alunoService, PersonalService personalService) {
        this.usuarioService = usuarioService;
        this.filterService = filterService;
        this.userDetails = userDetails;
        this.alunoService = alunoService;
        this.personalService = personalService;
    }

    @Operation(summary = "Realizar login (necessário cadastro)",
            description = "Endpoint para o login de usuários no sistema")
    @PostMapping("/login")
    public ResponseEntity<String> loginUsuario(@Valid @RequestBody ReqLoginUsuarioDTO dto,
                                               HttpServletResponse response) {
        Boolean isUsuarioEncontrado = usuarioService.loginUsuario(dto.email(), dto.senha());

        if(isUsuarioEncontrado){
            filterService.gerarCookie(response, dto.email());
            return ResponseEntity.ok().build();
        }

        return ResponseEntity.notFound().build();
    }

    @Operation(summary = "Excluir usuário (necessário login)",
            description = "Endpoint para a exclusão de usuários no sistema, " +
                    "onde o atributo 'ativo' define o seus status")
    @PatchMapping
    public ResponseEntity<?> deletarUsuario(@AuthenticationPrincipal UserDetails userDetails,
                                                  HttpServletResponse response) {
        String email = userDetails.getUsername();
        Boolean isUsuarioDeletado = usuarioService.removerUsuario(email);

        if(!isUsuarioDeletado){
            return ResponseEntity.notFound().build();
        }

        filterService.removerCookie(response);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Realizar logout",
            description = "Endpoint para o logout de usuários no sistema")
    @GetMapping("/logout")
    public ResponseEntity<?> logoutUsuario(HttpServletResponse response){
        filterService.removerCookie(response);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Buscar informações de usuario", description = "Endpoint para a busca as informações do usuario")
    @GetMapping("/me")
    public ResponseEntity<?> buscarEu(HttpServletResponse response){
        Usuario usuario = userDetails.getCurrentUser();

        if(usuario.getTipo() == TipoUsuario.ALUNO){
            ResBuscarAlunoPorIdDTO resUsuario = alunoService.buscarAlunoPorId(usuario.getId());

            return ResponseEntity.ok().body(resUsuario);

        }else if(usuario.getTipo() == TipoUsuario.PERSONAL){
            ResBuscarPersonalPorIdDTO resUsuario = personalService.buscarPersonalPorId(usuario.getId());
            return ResponseEntity.ok().body(resUsuario);
        }

        throw new UsuarioNaoEncontradoException();
    }


    @Operation(summary = "Adiciona imagem ao perfil do usuário", description = "Endpoint para adicionar uma imagem ao perfil do usuário")
    @PostMapping("/me/imagem")
    public ResponseEntity<?> adicionarImagemPerfil(@RequestParam("imagem") MultipartFile imagem) {
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

            return ResponseEntity.ok().body("Imagem atualizada com sucesso");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (IOException e) {
            return ResponseEntity.internalServerError().body("Erro ao salvar imagem");
        }
    }

    @Operation(summary = "Buscar imagem do perfil do usuário", description = "Endpoint para buscar a imagem do perfil do usuário")
    @GetMapping("/me/imagem")
    public ResponseEntity<Resource> buscarImagemPerfil() {
        try {
            Usuario usuario = userDetails.getCurrentUser();

            if (usuario.getCaminhoFoto() == null || usuario.getCaminhoFoto().isBlank()) {
                return ResponseEntity.notFound().build();
            }

            String nomeArquivo = Paths.get(usuario.getCaminhoFoto()).getFileName().toString();
            Resource resource = usuarioService.buscarFoto(nomeArquivo);

            return ResponseEntity.ok()
                    .header("Content-Type", "image/*")
                    .body(resource);
        } catch (IOException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Deletar imagem do perfil do usuário", description = "Endpoint para deletar a imagem do perfil do usuário")
    @DeleteMapping("/me/imagem")
    public ResponseEntity<?> deletarImagemPerfil() {
        try {
            Usuario usuario = userDetails.getCurrentUser();

            if (usuario.getCaminhoFoto() == null || usuario.getCaminhoFoto().isBlank()) {
                return ResponseEntity.notFound().build();
            }

            usuarioService.deletarFoto(usuario.getCaminhoFoto());
            usuario.setCaminhoFoto(null);
            usuarioService.salvarUsuario(usuario);

            return ResponseEntity.noContent().build();
        } catch (IOException e) {
            return ResponseEntity.internalServerError().body("Erro ao deletar imagem");
        }
    }

    @Operation(summary = "Buscar foto por nome", description = "Endpoint para buscar a foto do usuário por nome")
    @GetMapping("/foto/{nomeArquivo}")
    public ResponseEntity<Resource> buscarFotoPorNome(@PathVariable String nomeArquivo) {
        try {
            Usuario usuario = userDetails.getCurrentUser();

            Resource resource = usuarioService.buscarFoto(nomeArquivo);
            return ResponseEntity.ok()
                    .header("Content-Type", "image/*")
                    .body(resource);
        } catch (IOException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
