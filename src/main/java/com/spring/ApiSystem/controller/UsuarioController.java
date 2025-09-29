package com.spring.ApiSystem.controller;


import com.spring.ApiSystem.dto.usuario.request.LoginUsuarioDTO;
import com.spring.ApiSystem.model.User;
import com.spring.ApiSystem.dto.usuario.request.CadastroUsuarioDTO;
import com.spring.ApiSystem.dto.usuario.request.EditarUsuarioDTO;
import com.spring.ApiSystem.service.FilterService;
import com.spring.ApiSystem.service.UsuarioService;
import com.spring.ApiSystem.dto.usuario.response.ResUsuarioDTO;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/usuarios")
@RestController
public class UsuarioController {

    private final UsuarioService usuarioService;
    private final FilterService filterService;

    public UsuarioController(UsuarioService usuarioService, FilterService filterService) {
        this.usuarioService = usuarioService;
        this.filterService = filterService;
    }

    @PostMapping("/cadastro")
    public ResponseEntity<ResUsuarioDTO> cadastrarUsuario(@Valid @RequestBody CadastroUsuarioDTO cadastroUsuarioDTO) {
        return ResponseEntity.ok(usuarioService.cadastrarUsuario(cadastroUsuarioDTO));
    }

    @PostMapping("/login")
    public ResponseEntity<String> loginUsuario(@Valid @RequestBody LoginUsuarioDTO dto, HttpServletResponse response) {
        Boolean isUsuarioEncontrado = usuarioService.loginUsuario(dto.getEmail(), dto.getSenha());

        if(isUsuarioEncontrado){
            filterService.gerarCookie(response, dto.getEmail());
            return ResponseEntity.ok().build();
        }

        return ResponseEntity.notFound().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<User> atualizarUsuario(@PathVariable Long id, @Valid @RequestBody EditarUsuarioDTO dto) {
        User usuarioEditado = usuarioService.atualizarUsuario(id, dto);

        if(usuarioEditado == null){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(usuarioEditado);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<User> deletarUsuario(@PathVariable Long id) {
        Boolean isUsuarioDeletado = usuarioService.removerUsuario(id);

        if(!isUsuarioDeletado){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok().build();
    }
}
