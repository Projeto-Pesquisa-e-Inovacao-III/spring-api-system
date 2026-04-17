package com.spring.ApiSystem.domain.admin;

import com.spring.ApiSystem.domain.admin.dto.request.ReqCadastroPersonalDTO;
import com.spring.ApiSystem.domain.admin.dto.response.ResCadastrarPersonalDTO;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController()
@RequestMapping("/api/controle/admin")
public class AdminController {
        private final AdminService adminService;
        public AdminController(AdminService adminService) {
            this.adminService = adminService;
        }

    @Operation(summary = "Criar usuário", description = "Endpoint para cadastro de usuários no sistema")
    @PostMapping("/cadastrar-personal")
    public ResponseEntity<ResCadastrarPersonalDTO> cadastrarUsuario(
            @Valid @RequestBody ReqCadastroPersonalDTO cadastroUsuarioDTO
    ) {
        return ResponseEntity.ok(adminService.criarPersonal(cadastroUsuarioDTO));
    }


}
