package com.spring.ApiSystem.personal;

import com.spring.ApiSystem.personal.dto.request.ReqCadastroPersonalDTO;
import com.spring.ApiSystem.personal.dto.response.ResBuscarPersonalPorIdDTO;
import com.spring.ApiSystem.personal.dto.response.ResCadastrarPersonalDTO;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/personais")
public class PersonalController {

    private final PersonalService personalService;

    public PersonalController(PersonalService personalService) {
        this.personalService = personalService;
    }

    @Operation(summary = "Criar usuário", description = "Endpoint para cadastro de usuários no sistema")
    @PostMapping("/cadastro")
    public ResponseEntity<ResCadastrarPersonalDTO> cadastrarUsuario(@Valid @RequestBody ReqCadastroPersonalDTO cadastroUsuarioDTO) {
        return ResponseEntity.ok(personalService.cadastrarUsuario(cadastroUsuarioDTO));
    }


    @Operation (summary = "Buscar personal por ID (necessário login)",
            description = "Endpoint para buscar um personal específico pelo ID no sistema")
    @GetMapping("/{id}")
    public ResponseEntity<?> buscarPersonalPorId( @PathVariable  Long id) {
        ResBuscarPersonalPorIdDTO personal = personalService.buscarPersonalPorId(id);
        if(personal == null){
            return ResponseEntity.notFound().build();
        }
            return ResponseEntity.ok(personal);
    }
}
