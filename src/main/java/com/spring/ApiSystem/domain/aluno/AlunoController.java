package com.spring.ApiSystem.domain.aluno;

import com.spring.ApiSystem.domain.aluno.dto.request.ReqAtualizarAlunoDTO;
import com.spring.ApiSystem.domain.aluno.dto.request.ReqCadastroAlunoDTO;
import com.spring.ApiSystem.domain.aluno.dto.response.ResAtualizarAlunoDTO;
import com.spring.ApiSystem.domain.aluno.dto.response.ResAlunosPagantesDTO;
import com.spring.ApiSystem.domain.aluno.dto.response.ResBuscarAlunoPorIdDTO;
import com.spring.ApiSystem.domain.aluno.dto.response.ResCadastrarAlunoDTO;
import com.spring.ApiSystem.domain.aluno.dto.response.ResListarAlunosDto;
import com.spring.ApiSystem.domain.usuario.security.JpaUserDetailsService;
import com.spring.ApiSystem.shared.config.filter.FilterService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.SortDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.http.HttpResponse;
import java.util.List;

@RestController
@RequestMapping("/api/alunos")
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
    public ResponseEntity<ResCadastrarAlunoDTO> cadastrarUsuario(@Valid @RequestBody ReqCadastroAlunoDTO cadastroUsuarioDTO,
                                                                 HttpServletResponse response) {
        return ResponseEntity.ok(alunoService
                .cadastrarAluno(cadastroUsuarioDTO,response));
    }

    @Operation(summary = "Listar alunos (necessário login)",
               description = "Endpoint para listar alunos no sistema")
    @GetMapping
    public ResponseEntity<Page<ResListarAlunosDto>>
    listarAlunos(@SortDefault.SortDefaults({
                     @SortDefault(sort = "nome", direction = Sort.Direction.ASC),
                     @SortDefault(sort = "id", direction = Sort.Direction.ASC)
                 }) Pageable pageable,
                 @RequestParam(required = false) String nome) {
        Page<ResListarAlunosDto> alunos = alunoService.listarAlunos(pageable, nome);

        if(alunos.isEmpty()){
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(alunos);
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
    @PutMapping("/me/")
    public ResponseEntity<ResAtualizarAlunoDTO> atualizarAluno(@Valid @RequestBody ReqAtualizarAlunoDTO dto,
                                                               HttpServletResponse response) {

        Aluno usuario = userDetails.getCurrentAluno();
        ResAtualizarAlunoDTO usuarioEditado = alunoService.atualizarUsuario(dto, usuario);

        if(usuarioEditado == null){
            return ResponseEntity.notFound().build();
        }

        filterService.removerCookie(response);
        filterService.gerarCookie(response, usuarioEditado.email());
        return ResponseEntity.ok(usuarioEditado);
    }

    @Operation(summary = "Buscar quantidade de alunos com planos ativos",
            description = "Endpoint para buscar a quantidade de alunos pagantes (com planos ativos)")
    @GetMapping("/quantidade-ativos")
    public ResponseEntity<ResAlunosPagantesDTO> buscarAlunosAtivos() {
        return ResponseEntity.ok(alunoService.contarAlunosComPlanosAtivos());
    }
}
