package com.spring.ApiSystem.endereco;

import com.spring.ApiSystem.endereco.dto.request.EnderecoDTO;
import com.spring.ApiSystem.endereco.dto.response.BuscarEnderecoPorIdDTO;
import com.spring.ApiSystem.endereco.dto.response.ResEnderecoDTO;
import com.spring.ApiSystem.config.filter.FilterService;
import com.spring.ApiSystem.shared.security.token.TokenService;
import com.spring.ApiSystem.cep.ViaCepService;
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
    public ResponseEntity<ResEnderecoDTO> cadastrarEndereco(@Valid @RequestBody EnderecoDTO endereco,
                                                            HttpServletRequest request){

        String emailUsuario = tokenService.subjectToken(filterService.recuperarCookie(request));

        ResEnderecoDTO enderecoCadastrado = enderecoService.cadastrarEndereco(endereco, emailUsuario);

        if(enderecoCadastrado == null){
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok(enderecoCadastrado);
    }

    @Operation(summary = "Listar endereços (necessário login)",
            description = "Endpoint para listagem de endereços no sistema")
    @GetMapping
    public ResponseEntity<List<ResEnderecoDTO>> listarEnderecos(HttpServletRequest request){
        String emailUsuario = tokenService.subjectToken(filterService.recuperarCookie(request));

        return ResponseEntity.ok(enderecoService.listarEnderecos(emailUsuario));
    }

    @Operation(summary = "Editar endereço (necessário login)",
            description = "Endpoint para edição de endereços no sistema")
    @PutMapping("/{id}")
    public ResponseEntity<ResEnderecoDTO> atualizarEndereco(@PathVariable Long id,
                                            @Valid @RequestBody EnderecoDTO endereco,
                                            HttpServletRequest request){
        String emailUsuario = tokenService.subjectToken(filterService.recuperarCookie(request));
        ResEnderecoDTO enderecoEditado = enderecoService.atualizarEndereco(id, endereco, emailUsuario);

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

    @Operation(summary = "Buscar endereço por ID (necessário login)",
            description = "Endpoint para buscar um endereço específico pelo ID no sistema")
    @GetMapping("/{id}")
    public ResponseEntity<BuscarEnderecoPorIdDTO> buscarProdutosContratadosPorId(@PathVariable Long id){
        BuscarEnderecoPorIdDTO enderecoEncontrado = enderecoService.buscarPorId(id);
        if(enderecoEncontrado == null){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(enderecoEncontrado);
    }


}
