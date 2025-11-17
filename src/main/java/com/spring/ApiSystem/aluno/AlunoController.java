package com.spring.ApiSystem.aluno;

import com.spring.ApiSystem.aluno.dto.request.ReqAtualizarAlunoDTO;
import com.spring.ApiSystem.aluno.dto.request.ReqCadastroAlunoDTO;
import com.spring.ApiSystem.aluno.dto.response.ResAtualizarAlunoDTO;
import com.spring.ApiSystem.aluno.dto.response.ResBuscarAlunoPorIdDTO;
import com.spring.ApiSystem.aluno.dto.response.ResCadastrarAlunoDTO;
import com.spring.ApiSystem.config.filter.FilterService;
import com.spring.ApiSystem.usuario.Usuario;
import com.spring.ApiSystem.aluno.dto.response.ResListarAlunosDto;
import com.spring.ApiSystem.usuario.UsuarioService;
import com.spring.ApiSystem.usuario.dto.response.ResAtualizarUsuarioDTO;
import com.spring.ApiSystem.usuario.security.JpaUserDetailsService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/alunos")
public class AlunoController {

    private final AlunoService alunoService;
    private final FilterService filterService;
    private final JpaUserDetailsService userDetails;

    public AlunoController(AlunoService alunoService,
                           FilterService filterService,
                           JpaUserDetailsService userDetails) {
        this.alunoService = alunoService;
        this.filterService = filterService;
        this.userDetails = userDetails;
    }

    @Operation(summary = "Criar aluno",
               description = "Endpoint para cadastro de alunos no sistema")
    @PostMapping("/cadastro")
    public ResponseEntity<ResCadastrarAlunoDTO> cadastrarUsuario(@Valid @RequestBody ReqCadastroAlunoDTO cadastroUsuarioDTO) {
        return ResponseEntity.ok(alunoService.cadastrarUsuario(cadastroUsuarioDTO));
    }

    @Operation(summary = "Listar alunos (necessário login)",
               description = "Endpoint para listar alunos no sistema")
    @GetMapping
    public ResponseEntity<List<ResListarAlunosDto>> listarAlunos(@PageableDefault(sort = "nome")
                                                                 Pageable pageable) {
        return ResponseEntity.ok(alunoService.listarAlunos(pageable));
    }

    @Operation (summary = "Buscar aluno por ID (necessário login)",
                description = "Endpoint para buscar um aluno específico pelo ID no sistema")
    @GetMapping("/{id}")
    public ResponseEntity<?> buscarAlunoPorId( @PathVariable Long id) {
        ResBuscarAlunoPorIdDTO aluno = alunoService.buscarAlunoPorId(id);
        if(aluno == null){
            return ResponseEntity.notFound().build();
        }
            return ResponseEntity.ok(aluno);
    }


    @Operation(summary = "Editar aluno (necessário login)",
            description = "Endpoint para a edição de dados de aluno no sistema")
    @PutMapping("/me")
    public ResponseEntity<ResAtualizarAlunoDTO> atualizarAluno(@Valid @RequestBody ReqAtualizarAlunoDTO dto,
                                                               HttpServletResponse response) {

        Usuario usuario = userDetails.getCurrentUser();
        ResAtualizarAlunoDTO usuarioEditado = alunoService.atualizarUsuario(dto, usuario);

        if(usuarioEditado == null){
            return ResponseEntity.notFound().build();
        }

        filterService.removerCookie(response);
        filterService.gerarCookie(response, usuarioEditado.email());
        return ResponseEntity.ok(usuarioEditado);
    }
}
