package com.spring.ApiSystem.controller;

import com.spring.ApiSystem.dto.produtoExibicao.request.CadastroProdutoExibicaoDTO;
import com.spring.ApiSystem.dto.produtoExibicao.response.ResProdutoExibicaoDTO;
import com.spring.ApiSystem.service.ProdutoExibicaoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Produtos Exibição", description = "Operações relacionadas aos produtos que serão exibidos")
@RestController
@RequestMapping("/produtosExibicao")
public class ProdutoExibicaoController {
    private final ProdutoExibicaoService produtoExibicaoService;

    public ProdutoExibicaoController(ProdutoExibicaoService produtoExibicaoService) {
        this.produtoExibicaoService = produtoExibicaoService;
    }

    @Operation(summary = "Criar Produto de Exibição (necessário login)",
               description = "Endpoint para cadastro de produtos de exibição no sistema")
    @PostMapping
    public ResponseEntity<ResProdutoExibicaoDTO> criarProduto(@Valid @RequestBody CadastroProdutoExibicaoDTO produto){
        return ResponseEntity.ok(produtoExibicaoService.criarProduto(produto));
    }

    @Operation(summary = "Listar Produtos de Exibição por Status (necessário login)",
               description = "Endpoint para listagem de produtos de exibição por status no sistema")
    @GetMapping("/{status}")
    public ResponseEntity<List<ResProdutoExibicaoDTO>> listarProdutosPorStatus(@PathVariable String status){
        return ResponseEntity.ok(produtoExibicaoService.listarProdutosPorStatus(status));
    }

    @Operation(summary = "Listar Produtos de Exibição (necessário login)",
               description = "Endpoint para listagem de produtos de exibição no sistema")
    @GetMapping
    public ResponseEntity<List<ResProdutoExibicaoDTO>> listarProdutos(){
        return ResponseEntity.ok(produtoExibicaoService.listarProdutos());
    }

    @Operation(summary = "Desativar Produto de Exibição (necessário login)",
               description = "Endpoint para desativar produtos de exibição, alterando seu status para 'inativo'")
    @PatchMapping("/desativar/{id}")
    public ResponseEntity<?> desativarProduto(@PathVariable Long id) {
        produtoExibicaoService.desativarProduto(id);
        return ResponseEntity.noContent().build();
    }
}
