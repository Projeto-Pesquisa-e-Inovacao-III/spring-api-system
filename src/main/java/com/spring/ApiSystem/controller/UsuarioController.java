package com.spring.ApiSystem.controller;


import com.spring.ApiSystem.dto.usuario.request.LoginUsuarioDTO;
import com.spring.ApiSystem.model.Usuario;
import com.spring.ApiSystem.dto.usuario.request.CadastroUsuarioDTO;
import com.spring.ApiSystem.dto.usuario.request.EditarUsuarioDTO;
import com.spring.ApiSystem.service.FilterService;
import com.spring.ApiSystem.service.TokenService;
import com.spring.ApiSystem.service.UsuarioService;
import com.spring.ApiSystem.dto.usuario.response.ResUsuarioDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Usuários", description = "Operações relacionadas a usuários")
@RequestMapping("/usuarios")
@RestController
public class UsuarioController {

    private final UsuarioService usuarioService;
    private final FilterService filterService;
    private final TokenService tokenService;

    public UsuarioController(UsuarioService usuarioService,
                             FilterService filterService,
                             TokenService tokenService) {
        this.usuarioService = usuarioService;
        this.filterService = filterService;
        this.tokenService = tokenService;
    }

    @Operation(summary = "Criar usuário",
               description = "Endpoint para cadastro de usuários no sistema")
    @PostMapping("/cadastro")
    public ResponseEntity<ResUsuarioDTO> cadastrarUsuario(@Valid @RequestBody CadastroUsuarioDTO cadastroUsuarioDTO) {
        return ResponseEntity.ok(usuarioService.cadastrarUsuario(cadastroUsuarioDTO));
    }

//    @GetMapping("/listar")
//    public ResponseEntity listarUsuarios(){
//        return ResponseEntity.ok(usuarioService.listar());
//    }

    @Operation(summary = "Realizar login (necessário cadastro)",
            description = "Endpoint para o login de usuários no sistema")
    @PostMapping("/login")
    public ResponseEntity<String> loginUsuario(@Valid @RequestBody LoginUsuarioDTO dto,
                                               HttpServletResponse response) {
        Boolean isUsuarioEncontrado = usuarioService.loginUsuario(dto.email(), dto.senha());

        if(isUsuarioEncontrado){
            filterService.gerarCookie(response, dto.email());
            return ResponseEntity.ok().build();
        }

        return ResponseEntity.notFound().build();
    }

    @Operation(summary = "Realizar logout",
            description = "Endpoint para o logout de usuários no sistema")
    @GetMapping("/logout")
    public ResponseEntity<?> logoutUsuario(HttpServletResponse response){
        filterService.removerCookie(response);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Editar usuário (necessário login)",
            description = "Endpoint para a edição de dados de usuários no sistema")
    @PutMapping
    public ResponseEntity<Usuario> atualizarUsuario(@Valid @RequestBody EditarUsuarioDTO dto,
                                                    HttpServletRequest request,
                                                    HttpServletResponse response) {
        String valorCookie = filterService.recuperarCookie(request);
        String email = tokenService.subjectToken(valorCookie);
        Usuario usuarioEditado = usuarioService.atualizarUsuario(dto, email);

        if(usuarioEditado == null){
            return ResponseEntity.notFound().build();
        }

        filterService.removerCookie(response);
        filterService.gerarCookie(response, usuarioEditado.getEmail());
        return ResponseEntity.ok(usuarioEditado);
    }

    @Operation(summary = "Excluir usuário (necessário login)",
            description = "Endpoint para a exclusão de usuários no sistema, " +
                    "onde o atributo 'ativo' define o seus status")
    @PatchMapping
    public ResponseEntity<Usuario> deletarUsuario(HttpServletRequest request,
                                                  HttpServletResponse response) {
        String valorCookie = filterService.recuperarCookie(request);
        String email = tokenService.subjectToken(valorCookie);
        Boolean isUsuarioDeletado = usuarioService.removerUsuario(email);

        if(!isUsuarioDeletado){
            return ResponseEntity.notFound().build();
        }

        filterService.removerCookie(response);
        return ResponseEntity.noContent().build();
    }
}
