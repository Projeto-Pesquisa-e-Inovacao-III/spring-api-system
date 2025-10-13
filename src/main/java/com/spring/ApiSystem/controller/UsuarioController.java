package com.spring.ApiSystem.controller;


import com.spring.ApiSystem.dto.usuario.request.LoginUsuarioDTO;
import com.spring.ApiSystem.model.Usuario;
import com.spring.ApiSystem.dto.usuario.request.CadastroUsuarioDTO;
import com.spring.ApiSystem.dto.usuario.request.EditarUsuarioDTO;
import com.spring.ApiSystem.service.FilterService;
import com.spring.ApiSystem.service.TokenService;
import com.spring.ApiSystem.service.UsuarioService;
import com.spring.ApiSystem.dto.usuario.response.ResUsuarioDTO;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping("/cadastro")
    public ResponseEntity<ResUsuarioDTO> cadastrarUsuario(@Valid @RequestBody CadastroUsuarioDTO cadastroUsuarioDTO) {
        return ResponseEntity.ok(usuarioService.cadastrarUsuario(cadastroUsuarioDTO));
    }

    @PostMapping("/login")
    public ResponseEntity<String> loginUsuario(@Valid @RequestBody LoginUsuarioDTO dto,
                                               HttpServletResponse response) {
        Boolean isUsuarioEncontrado = usuarioService.loginUsuario(dto.getEmail(), dto.getSenha());

        if(isUsuarioEncontrado){
            filterService.gerarCookie(response, dto.getEmail());
            return ResponseEntity.ok().build();
        }

        return ResponseEntity.notFound().build();
    }

    @GetMapping("/logout")
    public ResponseEntity<?> LogoutUsuario(HttpServletResponse response){
        filterService.removerCookie(response);
        return ResponseEntity.ok().build();
    }

    @PutMapping
    public ResponseEntity<Usuario> atualizarUsuario(@Valid @RequestBody EditarUsuarioDTO dto,
                                                    HttpServletRequest request,
                                                    HttpServletResponse response) {
        String valorCookie = filterService.recuperarCookie(request);
        String email = tokenService.validarToken(valorCookie);
        Usuario usuarioEditado = usuarioService.atualizarUsuario(dto, email);

        if(usuarioEditado == null){
            return ResponseEntity.notFound().build();
        }

        filterService.removerCookie(response);
        filterService.gerarCookie(response, usuarioEditado.getEmail());
        return ResponseEntity.ok(usuarioEditado);
    }

    @PatchMapping
    public ResponseEntity<Usuario> deletarUsuario(HttpServletRequest request,
                                                  HttpServletResponse response) {
        String valorCookie = filterService.recuperarCookie(request);
        String email = tokenService.validarToken(valorCookie);
        Boolean isUsuarioDeletado = usuarioService.removerUsuario(email);

        if(!isUsuarioDeletado){
            return ResponseEntity.notFound().build();
        }

        filterService.removerCookie(response);
        return ResponseEntity.ok().build();
    }
}
