package com.spring.ApiSystem.personal;

import com.spring.ApiSystem.personal.dto.response.BuscarPersonalPorIdDTO;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/personais")
public class PersonalController {

    private final PersonalService personalService;

    public PersonalController(PersonalService personalService) {
        this.personalService = personalService;
    }

    @Operation (summary = "Buscar personal por ID (necessário login)",
            description = "Endpoint para buscar um personal específico pelo ID no sistema")
    @GetMapping("/{id}")
    public ResponseEntity<?> buscarPersonalPorId( @PathVariable  Long id) {
        BuscarPersonalPorIdDTO personal = personalService.buscarPersonalPorId(id);
        if(personal == null){
            return ResponseEntity.notFound().build();
        }
            return ResponseEntity.ok(personal);
    }
}
