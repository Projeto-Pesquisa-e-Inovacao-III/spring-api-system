package com.spring.ApiSystem.controller;

import com.spring.ApiSystem.dto.aluno.response.BuscarAlunoPorIdDTO;
import com.spring.ApiSystem.service.AlunoService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/alunos")
public class AlunoController {

    private final AlunoService alunoService;

    public AlunoController(AlunoService alunoService) {
        this.alunoService = alunoService;
    }



    @Operation (summary = "Buscar personal por ID (necessário login)",
            description = "Endpoint para buscar um aluno específico pelo ID no sistema")
    @GetMapping("/{id}")
    public ResponseEntity<?> buscarAlunoPorId( @PathVariable Integer id) {
        BuscarAlunoPorIdDTO aluno = alunoService.buscarAlunoPorId(id);
        if(aluno == null){
            return ResponseEntity.notFound().build();
        }
            return ResponseEntity.ok(aluno);
    }
}
