package com.spring.ApiSystem.domain.endereco;

import com.spring.ApiSystem.domain.endereco.dto.request.ReqAtualizarEnderecoDTO;
import com.spring.ApiSystem.domain.endereco.dto.request.ReqCadastrarEnderecoDTO;
import com.spring.ApiSystem.domain.endereco.dto.response.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Endereços", description = "Operações relacionadas a endereços")
@RestController
@RequestMapping("/api/enderecos")
public class EnderecoController {

    private final EnderecoService enderecoService;

    public EnderecoController(EnderecoService enderecoService) {
        this.enderecoService = enderecoService;
    }

    @Operation(
            summary = "Criar endereço (necessário login)",
            description = "Endpoint para cadastro de endereços no sistema"
    )
    @PostMapping
    public ResponseEntity<ResCadastrarEnderecoDTO> cadastrarEndereco(
            @Valid @RequestBody ReqCadastrarEnderecoDTO endereco,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        String emailUsuario = userDetails.getUsername();
        ResCadastrarEnderecoDTO enderecoCadastrado =
                enderecoService.cadastrarEndereco(endereco, emailUsuario, true);

        return ResponseEntity.ok(enderecoCadastrado);
    }

    @Operation(
            summary = "Editar endereço (necessário login)",
            description = "Endpoint para edição de endereços no sistema"
    )
    @PutMapping("/{id}")
    public ResponseEntity<ResAtualizarEnderecoDTO> atualizarEndereco(
            @PathVariable Long id,
            @Valid @RequestBody ReqAtualizarEnderecoDTO endereco,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        String emailUsuario = userDetails.getUsername();
        ResAtualizarEnderecoDTO enderecoEditado =
                enderecoService.atualizarEndereco(id, endereco, emailUsuario);

        return ResponseEntity.ok(enderecoEditado);
    }

    @Operation(
            summary = "Excluir endereço (necessário login)",
            description = "Endpoint para exclusão de endereços no sistema"
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> removerEndereco(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        String emailUsuario = userDetails.getUsername();
        enderecoService.removerEndereco(id, emailUsuario);
        return ResponseEntity.noContent().build();
    }

    @Operation(
            summary = "Buscar endereço por ID (necessário login)",
            description = "Endpoint para buscar um endereço específico pelo ID no sistema"
    )
    @GetMapping("/{id}")
    public ResponseEntity<ResBuscarEnderecoPorIdDTO> buscarProdutosContratadosPorId(@PathVariable Long id) {
        ResBuscarEnderecoPorIdDTO enderecoEncontrado = enderecoService.buscarPorIdDto(id);
        return ResponseEntity.ok(enderecoEncontrado);
    }

    @Operation(
            summary = "Listar endereços (necessário login)",
            description = "Endpoint para listagem de endereços no sistema"
    )
    @GetMapping
    public ResponseEntity<List<ResListarEnderecoDTO>> listarEnderecos() {
        return ResponseEntity.ok(enderecoService.listarEnderecos());
    }
}