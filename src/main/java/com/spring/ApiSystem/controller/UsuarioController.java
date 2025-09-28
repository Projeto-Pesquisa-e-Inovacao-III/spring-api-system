package com.spring.ApiSystem.controller;


import com.spring.ApiSystem.model.User;
import com.spring.ApiSystem.dto.usuario.request.CadastroUsuarioDTO;
import com.spring.ApiSystem.dto.usuario.request.EditarUsuarioDTO;
import com.spring.ApiSystem.service.UsuarioService;
import com.spring.ApiSystem.dto.usuario.response.ResUsuarioDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/usuarios")
@RestController
public class UsuarioController {

    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }
    
    @PostMapping("/cadastro")
    public ResponseEntity<ResUsuarioDTO> cadastrarUsuario(@RequestBody CadastroUsuarioDTO cadastroUsuarioDTO) {
        return ResponseEntity.ok(usuarioService.cadastrarUsuario(cadastroUsuarioDTO));
    }

    @PostMapping("/login")
    public ResponseEntity<String> loginUsuario(@RequestBody CadastroUsuarioDTO dto) {
        String token = usuarioService.loginUsuario(dto.getEmail(), dto.getSenha());

        return ResponseEntity.ok(token);
    }

    @PutMapping("/{id}")
    public ResponseEntity<User> atualizarUsuario(@PathVariable Long id, @RequestBody EditarUsuarioDTO dto) {
        return ResponseEntity.ok(usuarioService.atualizarUsuario(id, dto));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<User> deletarUsuario(@PathVariable Long id) {
        usuarioService.removerUsuario(id);
        return ResponseEntity.ok().build();
    }
}
