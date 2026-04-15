package com.spring.ApiSystem.domain.anamnese;

import com.spring.ApiSystem.domain.anamnese.dto.request.ReqAtualizarAnamneseDTO;
import com.spring.ApiSystem.domain.anamnese.dto.request.ReqCadastrarAnamneseDTO;
import com.spring.ApiSystem.domain.anamnese.dto.response.ResBuscarAnamneseDTO;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/anamnese")
public class AnamneseController {
    private final AnamneseService anamneseService;

    public AnamneseController(AnamneseService anamneseService) {
        this.anamneseService = anamneseService;
    }

    @Operation(summary = "Cadastrar uma nova anamnese", description = "Endpoint para cadastrar uma nova anamnese. Recebe os dados da anamnese no corpo da requisição e salva no banco de dados.")
    @PostMapping
    public ResponseEntity<Void> cadastrarAnamnese(@Valid @RequestBody ReqCadastrarAnamneseDTO anamnese) {

        anamneseService.cadastrarAnamnese(anamnese);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @Operation(summary = "Atualizar anamnese existente", description = "Endpoint para atualizar uma anamnese existente. Recebe os dados atualizados da anamnese no corpo da requisição e salva as alterações no banco de dados.")
    @PutMapping
    public ResponseEntity<Void> atualizarAnamnese(@Valid @RequestBody ReqAtualizarAnamneseDTO anamnese) {
        anamneseService.atualizarAnamnese(anamnese);

        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<ResBuscarAnamneseDTO> buscarAnamnese() {
        ResBuscarAnamneseDTO anamnese = anamneseService.buscarAnamnese();

        return ResponseEntity.ok(anamnese);
    }

    @GetMapping("/aluno/{id}")
    public ResponseEntity<ResBuscarAnamneseDTO> buscarAnamneseDoAluno(@PathVariable Long id) {
        ResBuscarAnamneseDTO anamnese = anamneseService.buscarAnamneseDoAluno(id);

        return ResponseEntity.ok(anamnese);
    }

}
