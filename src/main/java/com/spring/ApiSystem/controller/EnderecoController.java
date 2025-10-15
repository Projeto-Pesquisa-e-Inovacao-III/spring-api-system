package com.spring.ApiSystem.controller;

import com.spring.ApiSystem.dto.endereco.request.EdicaoEnderecoDTO;
import com.spring.ApiSystem.dto.endereco.request.EnderecoDTO;
import com.spring.ApiSystem.model.Endereco;
import com.spring.ApiSystem.service.EnderecoService;
import com.spring.ApiSystem.service.FilterService;
import com.spring.ApiSystem.service.TokenService;
import com.spring.ApiSystem.service.ViaCepService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Endereços", description = "Operações relacionadas a endereços")
@RestController
@RequestMapping("/enderecos")
public class EnderecoController {
    private final EnderecoService enderecoService;
    private final FilterService filterService;
    private final TokenService tokenService;

    public EnderecoController(EnderecoService enderecoService,
                              FilterService filterService,
                              TokenService tokenService,
                              ViaCepService viaCepService) {
        this.enderecoService = enderecoService;
        this.filterService = filterService;
        this.tokenService = tokenService;
    }

    @Operation(summary = "Criar endereço (necessário login)",
            description = "Endpoint para cadastro de endereços no sistema")
    @PostMapping
    public ResponseEntity<Endereco> cadastrarEndereco(@Valid @RequestBody EdicaoEnderecoDTO endereco,
                                                      HttpServletRequest request){

        String emailUsuario = tokenService.subjectToken(filterService.recuperarCookie(request));

        Endereco enderecoCadastrado = enderecoService.cadastrarEndereco(endereco, emailUsuario);

        if(enderecoCadastrado == null){
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok(enderecoCadastrado);
    }

    @Operation(summary = "Listar endereços (necessário login)",
            description = "Endpoint para listagem de endereços no sistema")
    @GetMapping
    public ResponseEntity<List<Endereco>> listarEnderecos(HttpServletRequest request){
        String emailUsuario = tokenService.subjectToken(filterService.recuperarCookie(request));

        return ResponseEntity.ok(enderecoService.listarEnderecos(emailUsuario));
    }

    @Operation(summary = "Editar endereço (necessário login)",
            description = "Endpoint para edição de endereços no sistema")
    @PutMapping("/{id}")
    public ResponseEntity<Endereco> atualizarEndereco(@PathVariable Long id,
                                            @Valid @RequestBody EdicaoEnderecoDTO endereco,
                                            HttpServletRequest request){
        String emailUsuario = tokenService.subjectToken(filterService.recuperarCookie(request));
        Endereco enderecoEditado = enderecoService.atualizarEndereco(id, endereco, emailUsuario);

        if(enderecoEditado == null){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(enderecoEditado);
    }

    @Operation(summary = "Excluir endereço (necessário login)",
            description = "Endpoint para exclusão de endereços no sistema")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> removerEndereco(@PathVariable Long id,
                                          HttpServletRequest request){
        String emailUsuario = tokenService.subjectToken(filterService.recuperarCookie(request));
        Boolean isEnderecoDeletado = enderecoService.removerEndereco(id, emailUsuario);

        if(!isEnderecoDeletado){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok().build();
    }
}
