package com.spring.ApiSystem.usuario;

import com.spring.ApiSystem.usuario.dto.request.ReqLoginUsuarioDTO;
import com.spring.ApiSystem.usuario.dto.request.ReqCadastroUsuarioDTO;
import com.spring.ApiSystem.usuario.dto.request.ReqEditarUsuarioDTO;
import com.spring.ApiSystem.config.filter.FilterService;
import com.spring.ApiSystem.usuario.dto.response.ResAtualizarUsuarioDTO;
import com.spring.ApiSystem.usuario.dto.response.ResCadastrarUsuarioDTO;
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

    public UsuarioController(UsuarioService usuarioService,
                             FilterService filterService) {
        this.usuarioService = usuarioService;
        this.filterService = filterService;
    }

    @Operation(summary = "Criar usuário",
               description = "Endpoint para cadastro de usuários no sistema")
    @PostMapping("/cadastro")
    public ResponseEntity<ResCadastrarUsuarioDTO> cadastrarUsuario(@Valid @RequestBody ReqCadastroUsuarioDTO cadastroUsuarioDTO) {
        return ResponseEntity.ok(usuarioService.cadastrarUsuario(cadastroUsuarioDTO));
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

}
