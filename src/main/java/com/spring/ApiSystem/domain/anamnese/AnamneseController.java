package com.spring.ApiSystem.domain.anamnese;

import com.spring.ApiSystem.domain.anamnese.dto.request.ReqCadastroAnamneseDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    public ResponseEntity<?> cadastrarAnamnese(@Valid @RequestBody ReqCadastroAnamneseDto anamnese){
        Anamnese novaAnamnese = anamneseService.cadastrarAnamnese(anamnese);
        return ResponseEntity.ok(novaAnamnese);

    }
}
