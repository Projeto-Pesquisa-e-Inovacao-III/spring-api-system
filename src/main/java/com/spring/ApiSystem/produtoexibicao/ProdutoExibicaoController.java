package com.spring.ApiSystem.produtoexibicao;

import com.spring.ApiSystem.produtoexibicao.dto.request.CadastroProdutoExibicaoDTO;
import com.spring.ApiSystem.produtoexibicao.dto.request.EdicaoProdutoExibicaoDTO;
import com.spring.ApiSystem.produtoexibicao.dto.response.ResListaProdutoExibicaoDTO;
import com.spring.ApiSystem.produtoexibicao.dto.response.ResProdutoExibicaoDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Produtos Exibição", description = "Operações relacionadas aos produtos que serão exibidos")
@RestController
@RequestMapping("/produtos-exibicoes")
public class ProdutoExibicaoController {
    private final ProdutoExibicaoService produtoExibicaoService;

    public ProdutoExibicaoController(ProdutoExibicaoService produtoExibicaoService) {
        this.produtoExibicaoService = produtoExibicaoService;
    }

    @Operation(summary = "Criar Produto de Exibição",
               description = "Endpoint para cadastro de produtos de exibição no sistema")
    @PostMapping
    public ResponseEntity<ResProdutoExibicaoDTO> criarProduto(@Valid @RequestBody CadastroProdutoExibicaoDTO produto){
        return ResponseEntity.ok(produtoExibicaoService.criarProduto(produto));
    }

    @Operation(summary = "Editar Produto de Exibição",
            description = "Endpoint para edição de produtos de exibição no sistema," +
                    "onde é gerado um novo produto com as informações atualizadas")
    @PostMapping("/editar/{id}")
    public ResponseEntity<ResProdutoExibicaoDTO>
    editarProduto(@PathVariable Long id,
                  @Valid @RequestBody EdicaoProdutoExibicaoDTO produto){
        return ResponseEntity.ok(produtoExibicaoService.editarProduto(id, produto));
    }

    @Operation(summary = "Listar Produtos de Exibição por Status",
               description = "Endpoint para listagem de produtos de exibição por status no sistema")
    @GetMapping("/status/{status}")
    public ResponseEntity<List<ResProdutoExibicaoDTO>> listarProdutosPorStatus(@PathVariable String status){
        return ResponseEntity.ok(produtoExibicaoService.listarProdutosPorStatus(status));
    }

    @Operation(summary = "Listar Produtos de Exibição",
               description = "Endpoint para listagem de produtos de exibição no sistema")
    @GetMapping
    public ResponseEntity<List<ResListaProdutoExibicaoDTO>> listarProdutos(){
        return ResponseEntity.ok(produtoExibicaoService.listarProdutos());
    }

    @Operation(summary = "Desativar Produto de Exibição",
               description = "Endpoint para desativar produtos de exibição, alterando seu status para 'inativo'")
    @PatchMapping("/desativar/{id}")
    public ResponseEntity<?> desativarProduto(@PathVariable Long id) {
        produtoExibicaoService.desativarProduto(id);
        return ResponseEntity.noContent().build();
    }
}
