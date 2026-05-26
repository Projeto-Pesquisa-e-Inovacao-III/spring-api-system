package com.spring.ApiSystem.domain.admin;

import com.spring.ApiSystem.domain.admin.dto.request.ReqAdicionarRoleDTO;
import com.spring.ApiSystem.domain.admin.dto.request.ReqCadastroAdminDTO;
import com.spring.ApiSystem.domain.admin.dto.request.ReqCadastroPersonalDTO;
import com.spring.ApiSystem.domain.admin.dto.response.ResRoleNeedDataDTO;
import com.spring.ApiSystem.domain.admin.dto.response.ResUsuarioWithRolesResponseDTO;
import com.spring.ApiSystem.domain.usuario.enums.Role;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController()
@RequestMapping("/api/admin")
public class AdminController {
        private final AdminService adminService;
        public AdminController(AdminService adminService) {
            this.adminService = adminService;
        }


    @Operation(summary = "Criar admin", description = "Endpoint para cadastro de administradores no sistema")
    @PostMapping("/usuarios/admin")
    @ResponseStatus(HttpStatus.CREATED)
    public void criarAdmin(
            @Valid @RequestBody ReqCadastroAdminDTO cadastroUsuarioDTO
    ) {
        adminService.createAdminUser(cadastroUsuarioDTO);
    }

    @Operation(summary = "Criar personal", description = "Endpoint para cadastro de personals no sistema")
    @PostMapping("/usuarios/personal")
    @ResponseStatus(HttpStatus.CREATED)
    public void criarPersonal(
            @Valid @RequestBody ReqCadastroPersonalDTO cadastroUsuarioDTO
    ) {
        adminService.createPersonalUser(cadastroUsuarioDTO);
    }

    @Operation(summary = "Adicionar role a usuário", description = "Endpoint para adicionar uma role a um usuário existente no sistema")
    @PutMapping("/usuarios/{id}/roles")
    public ResponseEntity<Void> adicionarRoleAUsuario(
            @Valid @RequestBody(required = false) ReqAdicionarRoleDTO reqAdicionarRoleDTO,
            @RequestParam Role role,
            @PathVariable Long id
    ) {
        adminService.addRoleToUser(role, id, reqAdicionarRoleDTO);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Remover role a usuário", description = "Endpoint para remover uma role a um usuário existente no sistema")
    @DeleteMapping("/usuarios/{id}/roles")
    public ResponseEntity<Void> adicionarRoleAUsuario(
            @RequestParam Role role,
            @PathVariable Long id
    ) {
        adminService.removeRole(role, id);
        return ResponseEntity.noContent().build();
    }


    @Operation(summary = "Verificar dados adicioais para a role.", description = "Endpoint para verificar se um usuário precisa fornecer dados adicionais para receber uma role específica, como por exemplo, a role de PERSONAL, que exige o fornecimento do número do CREF.")
    @GetMapping("/usuarios/{id}/roles/perfil")
    public ResponseEntity<ResRoleNeedDataDTO> verificarDadosAdicionaisParaRole(
            @PathVariable Long id,
            @RequestParam Role role
    ) {
        return ResponseEntity.ok(adminService.verifyNeedExtraData(id, role));
    }

    @Operation(
            summary = "Excluir usuário",
            description = "Endpoint para a exclusão de usuários no sistema marcando como inativo e retirando suas roles, sem deletar o registro do banco de dados."
    )
    @PatchMapping("/usuarios/{id}/deletar")
    public ResponseEntity<Void> deletarUsuario(
            @PathVariable Long id
    ) {
        adminService.softDelete(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(
            summary = "Listar usuários com roles",
            description = "Endpoint para listar os usuários do sistema com suas respectivas roles, com paginação e filtros opcionais por nome e email."
    )
    @GetMapping("/usuarios")
    public ResponseEntity<Page<ResUsuarioWithRolesResponseDTO>> listarUsuariosComRoles(
            @RequestParam(required = false) String nome,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) Role role,
            Pageable pageable
    ) {
        return ResponseEntity.ok(adminService.listUsersWithFilter(nome, email, role, pageable));
    }




}
