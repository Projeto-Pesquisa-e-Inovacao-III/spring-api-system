package com.spring.ApiSystem.domain.produtoexibicao;

import com.spring.ApiSystem.domain.produtoexibicao.dto.request.ReqCadastroProdutoExibicaoDto;
import com.spring.ApiSystem.domain.produtoexibicao.dto.request.ReqEdicaoProdutoExibicaoDto;
import com.spring.ApiSystem.domain.produtoexibicao.dto.response.ResProdutoExibicaoDto;
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
    public ResponseEntity<ResProdutoExibicaoDto> criarProduto(@Valid @RequestBody ReqCadastroProdutoExibicaoDto produto){
        return ResponseEntity.ok(produtoExibicaoService.criarProduto(produto));
    }

    @Operation(summary = "Editar Produto de Exibição",
            description = "Endpoint para edição de produtos de exibição no sistema," +
                    "onde é gerado um novo produto com as informações atualizadas")
    @PostMapping("/editar/{id}")
    public ResponseEntity<ResProdutoExibicaoDto> editarProduto(@PathVariable Long id,
                  @Valid @RequestBody ReqEdicaoProdutoExibicaoDto produto){
        return ResponseEntity.ok(produtoExibicaoService.editarProduto(id, produto));
    }

    @Operation(summary = "Listar Produtos de Exibição por Status",
               description = "Endpoint para listagem de produtos de exibição por status no sistema")
    @GetMapping("/status/{status}")
    public ResponseEntity<List<ResProdutoExibicaoDto>> listarProdutosPorStatus(@PathVariable String status){
        return ResponseEntity.ok(produtoExibicaoService.listarProdutosPorStatus(status));
    }

    @Operation(summary = "Listagem de Produtos de Exibição por Status ativo",
    description = "Endpoint para listagem de produtos de exibição com status ativo. É para ser utilizado no site institucional")
    @GetMapping("/ativos")
    public ResponseEntity<List<ResProdutoExibicaoDto>> listarProdutosAtivos(){
        return ResponseEntity.ok(produtoExibicaoService.listarProdutosAtivos());
    }

    @Operation(summary = "Listar Produtos de Exibição",
               description = "Endpoint para listagem de produtos de exibição no sistema")
    @GetMapping
    public ResponseEntity<List<ResProdutoExibicaoDto>> listarProdutos(){
        return ResponseEntity.ok(produtoExibicaoService.listarProdutos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResProdutoExibicaoDto> buscarPorId(@PathVariable Long id){
        return ResponseEntity.ok(produtoExibicaoService.resBuscarPorId(id));
    }

    @Operation(summary = "Desativar Produto de Exibição",
               description = "Endpoint para desativar produtos de exibição, alterando seu status para 'inativo'")
    @PatchMapping("/desativar/{id}")
    public ResponseEntity<?> desativarProduto(@PathVariable Long id) {
        produtoExibicaoService.desativarProduto(id);
        return ResponseEntity.noContent().build();
    }
}
