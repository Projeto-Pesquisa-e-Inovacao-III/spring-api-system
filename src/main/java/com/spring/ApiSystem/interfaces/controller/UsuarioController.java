package com.spring.ApiSystem.interfaces.controller;


import com.spring.ApiSystem.application.service.CadastroUsuarioDTO;
import com.spring.ApiSystem.application.service.EditarUsuarioDTO;
import com.spring.ApiSystem.application.service.UsuarioService;
import com.spring.ApiSystem.domain.entity.UsuarioEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Provider;

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


    @DeleteMapping
    public ResponseEntity<UsuarioEntity> deletarUsuario(@PathVariable Long id) {
        usuarioService.removerUsuario(id);
        return ResponseEntity.ok().build();
    }


}
