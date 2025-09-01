package com.spring.ApiSystem.interfaces.controller;


import com.spring.ApiSystem.application.dto.CadastroUsuarioDTO;
import com.spring.ApiSystem.application.dto.EditarUsuarioDTO;
import com.spring.ApiSystem.application.service.UsuarioService;
import com.spring.ApiSystem.domain.entity.UsuarioEntity;
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
    public ResponseEntity<UsuarioEntity> cadastrarUsuario(@RequestBody UsuarioEntity usuarioEntity){
        return ResponseEntity.ok(usuarioService.cadastrarUsuario(usuarioEntity));
    }

    @PostMapping("/login")
    public ResponseEntity<UsuarioEntity> loginUsuario(@RequestBody CadastroUsuarioDTO dto) {
        return usuarioService.loginUsuario(dto.getEmail(), dto.getSenha())
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.status(401).build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<UsuarioEntity> atualizarUsuario(@PathVariable Long id, @RequestBody EditarUsuarioDTO dto) {
        return ResponseEntity.ok(usuarioService.atualizarUsuario(id, dto));
    }

    // Alterar para PatchMapping pois irá apenas atualizar para true or false
    @PatchMapping("/{id}")
    public ResponseEntity<UsuarioEntity> deletarUsuario(@PathVariable Long id) {
        usuarioService.removerUsuario(id);
        return ResponseEntity.ok().build();
    }


}
