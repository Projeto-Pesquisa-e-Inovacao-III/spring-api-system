package com.spring.ApiSystem.produtocontratado;

import com.spring.ApiSystem.produtocontratado.dto.request.CriarProdutoContratadoDto;
import com.spring.ApiSystem.produtocontratado.dto.request.EditarProdutoContratadoDto;
import com.spring.ApiSystem.produtocontratado.dto.response.BuscarProdutoContratadoPorIdDto;
import com.spring.ApiSystem.produtocontratado.dto.response.ProdutoContratadoDto;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<ProdutoContratadoDto> criarProdutoContratado(@Valid @RequestBody CriarProdutoContratadoDto
                                                                       criarProdutoContratadoDto){
        ProdutoContratadoDto produtoContratadoDto = produtoContratadoService.criarProdutoContratado(
                criarProdutoContratadoDto.idProdutoExibicao(),
                criarProdutoContratadoDto.idAluno()
        );

        return ResponseEntity.ok(produtoContratadoDto);
    }

    @Operation(summary = "Lista todos os produtos contratados (necessário login)",
              description = "Endpoint para listar todos os produtos contratados em sistema")
    @GetMapping
    public ResponseEntity<List<ProdutoContratadoDto>> listarProdutosContratados(){
        List<ProdutoContratadoDto> produtosContratados = produtoContratadoService.listarProdutosContratados();
        return ResponseEntity.ok(produtosContratados);
    }

    @Operation(summary = "Lista dos produtos contratados com base na situacao(necessário login)",
            description = "Endpoint para listar todos os produtos contratados em sistema com base" +
                    "na situacao informada")
    @GetMapping("/situacao/{situacao}")
    public ResponseEntity<List<ProdutoContratadoDto>>
    listarProdutosContratadosPorSituacao(@PathVariable Boolean situacao){
        List<ProdutoContratadoDto> produtosContratados = produtoContratadoService.listarPorSituacao(situacao);
        return ResponseEntity.ok(produtosContratados);
    }

    @Operation(summary = "Lista o produto contratado com base no ID (necessário login)",
            description = "Endpoint para listar o produto contratado com base no ID informado")
    @GetMapping("/id/{id}")
    public ResponseEntity<BuscarProdutoContratadoPorIdDto>
    listarProdutosContratadosPorId(@PathVariable Long id){
        BuscarProdutoContratadoPorIdDto produtoContratado = produtoContratadoService.listarPorIdDto(id);
        if(produtoContratado == null){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(produtoContratado);
    }

    @Operation(summary = "Lista todos os produtos contratados do usuário (necessário login)",
            description = "Endpoint para listar todos os produtos contratados em sistema que" +
                    "tiverem o idAluno correspondente")
    @GetMapping("/idAluno/{id}")
    public ResponseEntity<List<ProdutoContratadoDto>>
    listarProdutosContratadosPorIdAluno(@PathVariable Long id){
        List<ProdutoContratadoDto> produtosContratados = produtoContratadoService.listarPorAluno(id);
        return ResponseEntity.ok(produtosContratados);
    }

    @Operation(summary = "Edita um produto contratado (necessário login)",
               description = "Endpoint para editar um produto contratado")
    @PutMapping
    public ResponseEntity<ProdutoContratadoDto> atualizarProdutoContratado(@Valid @RequestBody
                                                                           EditarProdutoContratadoDto
                                                                           editarProdutoContratadoDto){
        ProdutoContratadoDto produtoContratadoDto = produtoContratadoService.atualizarProdutoContratado(
                editarProdutoContratadoDto);

        return ResponseEntity.ok(produtoContratadoDto);
    }

    @Operation(summary = "Desativa um produto contratado (necessário login)",
               description = "Endpoint para desativar um produto contratado")
    @PatchMapping("/desativar/{id}")
    public ResponseEntity<ProdutoContratadoDto> desativarProdutoContratado(@PathVariable Long id){
        produtoContratadoService.desativarProdutoContratado(id);

        return ResponseEntity.noContent().build();
    }
}
