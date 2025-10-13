package com.spring.ApiSystem.controller;

import com.spring.ApiSystem.dto.endereco.request.EnderecoDTO;
import com.spring.ApiSystem.model.Endereco;
import com.spring.ApiSystem.service.EnderecoService;
import com.spring.ApiSystem.service.FilterService;
import com.spring.ApiSystem.service.TokenService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/enderecos")
public class EnderecoController {
    private final EnderecoService enderecoService;
    private final FilterService filterService;
    private final TokenService tokenService;

    public EnderecoController(EnderecoService enderecoService,
                              FilterService filterService,
                              TokenService tokenService) {
        this.enderecoService = enderecoService;
        this.filterService = filterService;
        this.tokenService = tokenService;
    }

    @PostMapping
    public ResponseEntity<Endereco> cadastrarEndereco(@Valid @RequestBody EnderecoDTO endereco,
                                            HttpServletRequest request){
        String emailUsuario = tokenService.validarToken(filterService.recuperarCookie(request));

        Endereco enderecoCadastrado = enderecoService.cadastrarEndereco(endereco, emailUsuario);

        if(enderecoCadastrado == null){
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok(enderecoCadastrado);
    }

    @GetMapping
    public ResponseEntity<List<Endereco>> listarEnderecos(HttpServletRequest request){
        String emailUsuario = tokenService.validarToken(filterService.recuperarCookie(request));

        return ResponseEntity.ok(enderecoService.listarEnderecos(emailUsuario));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Endereco> atualizarEndereco(@PathVariable Long id,
                                            @Valid @RequestBody EnderecoDTO endereco,
                                            HttpServletRequest request){
        String emailUsuario = tokenService.validarToken(filterService.recuperarCookie(request));
        Endereco enderecoEditado = enderecoService.atualizarEndereco(id, endereco, emailUsuario);

        if(enderecoEditado == null){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(enderecoEditado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> removerEndereco(@PathVariable Long id,
                                          HttpServletRequest request){
        String emailUsuario = tokenService.validarToken(filterService.recuperarCookie(request));
        Boolean isEnderecoDeletado = enderecoService.removerEndereco(id, emailUsuario);

        if(!isEnderecoDeletado){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok().build();
    }
}
