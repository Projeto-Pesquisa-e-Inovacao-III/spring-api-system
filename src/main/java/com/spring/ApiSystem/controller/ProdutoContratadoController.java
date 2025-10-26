package com.spring.ApiSystem.controller;

import com.spring.ApiSystem.dto.ProdutoContratado.response.BuscarProdutoContratadoPorId;
import com.spring.ApiSystem.service.ProdutoContratadoService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/produtos-contratados")
public class ProdutoContratadoController {
    private final ProdutoContratadoService produtoContratadoService;

    public ProdutoContratadoController(ProdutoContratadoService produtoContratadoService) {
        this.produtoContratadoService = produtoContratadoService;
    }

    @Operation(summary = "Buscar contrato por ID (necessário login)",
            description = "Endpoint para buscar um contrato específico pelo ID no sistema")
    @GetMapping("/{id}")
    public ResponseEntity<?> buscarProdutosContratadosPorId(@PathVariable Integer id){
        BuscarProdutoContratadoPorId produtoContratado = produtoContratadoService.buscarPorIdProdutoContratado(id);
        if(produtoContratado == null){
            return ResponseEntity.notFound().build();
        }
            return ResponseEntity.ok(produtoContratado);
    }
}
