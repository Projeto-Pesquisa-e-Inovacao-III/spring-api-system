package com.spring.ApiSystem.produtocontratado;

import com.spring.ApiSystem.produtocontratado.dto.request.CriarProdutoContratadoDto;
import com.spring.ApiSystem.produtocontratado.dto.response.ResBuscarProdutoContratadoPorIdDto;
import com.spring.ApiSystem.produtocontratado.dto.response.ResProdutoContratadoDto;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/produtos-contratados")
public class ProdutoContratadoController {
    private final ProdutoContratadoService produtoContratadoService;

    public ProdutoContratadoController(ProdutoContratadoService produtoContratadoService) {
        this.produtoContratadoService = produtoContratadoService;
    }

    @Operation(summary = "Cria um produto contratado (necessário login)",
              description = "Endpoint para criar um produto contratado com base no ID do produto" +
                      "de exibição e no ID do aluno")
    @PostMapping
    public ResponseEntity<ResProdutoContratadoDto>
    criarProdutoContratado(@Valid @RequestBody CriarProdutoContratadoDto
                           criarProdutoContratadoDto,
                           @AuthenticationPrincipal UserDetails userDetails){
        ResProdutoContratadoDto resProdutoContratadoDto = produtoContratadoService.criarProdutoContratado(
                criarProdutoContratadoDto.idProdutoExibicao(),
                userDetails.getUsername()
        );

        return ResponseEntity.ok(resProdutoContratadoDto);
    }

    @Operation(summary = "Lista todos os produtos contratados",
              description = "Endpoint para listar todos os produtos contratados em sistema")
    @GetMapping
    public ResponseEntity<List<ResProdutoContratadoDto>> listarProdutosContratados(){
        List<ResProdutoContratadoDto> produtosContratados = produtoContratadoService.listarProdutosContratados();
        return ResponseEntity.ok(produtosContratados);
    }

    @Operation(summary = "Lista dos produtos contratados com base na situacao",
            description = "Endpoint para listar todos os produtos contratados em sistema com base" +
                    "na situacao informada")
    @GetMapping("/situacao/{situacao}")
    public ResponseEntity<List<ResProdutoContratadoDto>>
    listarProdutosContratadosPorSituacao(@PathVariable Boolean situacao){
        List<ResProdutoContratadoDto> produtosContratados = produtoContratadoService.listarPorSituacao(situacao);
        return ResponseEntity.ok(produtosContratados);
    }

    @Operation(summary = "Lista o produto contratado com base no ID (necessário login)",
            description = "Endpoint para listar o produto contratado com base no ID informado")
    @GetMapping("/id/{id}")
    public ResponseEntity<ResProdutoContratadoDto>
    buscarProdutoContratadoPorId(@PathVariable Long id,
                                   @AuthenticationPrincipal UserDetails userDetails){
        ResProdutoContratadoDto produtoContratado = produtoContratadoService
                .buscarPorIdAlunoEmail(id, userDetails.getUsername());
        if(produtoContratado == null){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(produtoContratado);
    }

    @Operation(summary = "Lista todos os produtos contratados do usuário (necessário login)",
            description = "Endpoint para listar todos os produtos contratados em sistema que" +
                    "tiverem o idAluno correspondente")
    @GetMapping("/aluno")
    public ResponseEntity<List<ResProdutoContratadoDto>>
    listarProdutosContratadosPorIdAluno(@PageableDefault(sort = "dataCompra", direction = Sort.Direction.DESC)
                                        Pageable pageable,
                                        @AuthenticationPrincipal UserDetails userDetails){
        List<ResProdutoContratadoDto> produtosContratados = produtoContratadoService
                .listarPorAluno(userDetails.getUsername(), pageable);
        return ResponseEntity.ok(produtosContratados);
    }

    @Operation(summary = "Desativa um produto contratado",
               description = "Endpoint para desativar um produto contratado")
    @PatchMapping("/desativar/{id}")
    public ResponseEntity<ResProdutoContratadoDto> desativarProdutoContratado(@PathVariable Long id){
        produtoContratadoService.desativarProdutoContratado(id);

        return ResponseEntity.noContent().build();
    }
}
