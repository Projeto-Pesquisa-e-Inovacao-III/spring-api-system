package com.spring.ApiSystem.usuario;

import com.spring.ApiSystem.aluno.AlunoService;
import com.spring.ApiSystem.aluno.dto.response.BuscarAlunoPorIdDTO;
import com.spring.ApiSystem.personal.PersonalService;
import com.spring.ApiSystem.personal.dto.response.BuscarPersonalPorIdDTO;
import com.spring.ApiSystem.usuario.dto.request.ReqLoginUsuarioDTO;
import com.spring.ApiSystem.usuario.dto.request.ReqCadastroUsuarioDTO;
import com.spring.ApiSystem.usuario.dto.request.ReqEditarUsuarioDTO;
import com.spring.ApiSystem.config.filter.FilterService;
import com.spring.ApiSystem.usuario.dto.response.ResAtualizarUsuarioDTO;
import com.spring.ApiSystem.usuario.dto.response.ResBuscarUsuarioPorEmailDTO;
import com.spring.ApiSystem.usuario.dto.response.ResCadastrarUsuarioDTO;
import com.spring.ApiSystem.usuario.enums.TipoUsuario;
import com.spring.ApiSystem.usuario.exception.UsuarioNaoEncontradoException;
import com.spring.ApiSystem.usuario.security.JpaUserDetailsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

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

    @Operation(summary = "Editar usuário (necessário login)",
            description = "Endpoint para a edição de dados de usuários no sistema")
    @PutMapping
    public ResponseEntity<ResAtualizarUsuarioDTO> atualizarUsuario(@Valid @RequestBody ReqEditarUsuarioDTO dto,
                                                                   @AuthenticationPrincipal UserDetails userDetails,
                                                                   HttpServletResponse response) {

        String email = userDetails.getUsername();
        ResAtualizarUsuarioDTO usuarioEditado = usuarioService.atualizarUsuario(dto, email);

        if(usuarioEditado == null){
            return ResponseEntity.notFound().build();
        }

        filterService.removerCookie(response);
        filterService.gerarCookie(response, usuarioEditado.email());
        return ResponseEntity.ok(usuarioEditado);
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
            BuscarAlunoPorIdDTO resUsuario = alunoService.buscarAlunoPorId(usuario.getId());
            return ResponseEntity.ok().body(resUsuario);

        }else if(usuario.getTipo() == TipoUsuario.PERSONAL){
            BuscarPersonalPorIdDTO resUsuario = personalService.buscarPersonalPorId(usuario.getId());
            return ResponseEntity.ok().body(resUsuario);
        }

        throw new UsuarioNaoEncontradoException();
    }
}
