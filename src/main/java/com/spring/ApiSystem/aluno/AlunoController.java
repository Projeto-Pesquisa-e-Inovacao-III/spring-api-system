package com.spring.ApiSystem.aluno;

import com.spring.ApiSystem.aluno.dto.request.ReqCadastroAlunoDTO;
import com.spring.ApiSystem.aluno.dto.response.ResBuscarAlunoPorIdDTO;
import com.spring.ApiSystem.aluno.dto.response.ResCadastrarAlunoDTO;
import com.spring.ApiSystem.usuario.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/alunos")
public class AlunoController {

    private final AlunoService alunoService;
    private final UsuarioService usuarioService;

    public AlunoController(AlunoService alunoService, UsuarioService usuarioService) {
        this.alunoService = alunoService;
        this.usuarioService = usuarioService;
    }

    @Operation(summary = "Criar usuário", description = "Endpoint para cadastro de usuários no sistema")
    @PostMapping("/cadastro")
    public ResponseEntity<ResCadastrarAlunoDTO> cadastrarUsuario(@Valid @RequestBody ReqCadastroAlunoDTO cadastroUsuarioDTO) {
        return ResponseEntity.ok(alunoService.cadastrarUsuario(cadastroUsuarioDTO));
    }

    @Operation (summary = "Buscar aluno por ID (necessário login)", description = "Endpoint para buscar um aluno específico pelo ID no sistema")
    @GetMapping("/{id}")
    public ResponseEntity<?> buscarAlunoPorId( @PathVariable Long id) {
        ResBuscarAlunoPorIdDTO aluno = alunoService.buscarAlunoPorId(id);
        if(aluno == null){
            return ResponseEntity.notFound().build();
        }
            return ResponseEntity.ok(aluno);
    }
}
