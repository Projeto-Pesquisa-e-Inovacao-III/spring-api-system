package com.spring.ApiSystem.personal;

import com.spring.ApiSystem.aluno.dto.response.ResListarAlunosDto;
import com.spring.ApiSystem.personal.dto.request.ReqAtualizarBufferDTO;
import com.spring.ApiSystem.aluno.dto.request.ReqAtualizarAlunoDTO;
import com.spring.ApiSystem.aluno.dto.response.ResAtualizarAlunoDTO;
import com.spring.ApiSystem.config.filter.FilterService;
import com.spring.ApiSystem.personal.dto.request.ReqAtualizarPersonalDTO;
import com.spring.ApiSystem.personal.dto.request.ReqAtualizarBufferDTO;
import com.spring.ApiSystem.personal.dto.request.ReqCadastroPersonalDTO;
import com.spring.ApiSystem.personal.dto.response.ResAtualizarPersonalDTO;
import com.spring.ApiSystem.personal.dto.response.ResBuscarPersonalPorIdDTO;
import com.spring.ApiSystem.personal.dto.response.ResCadastrarPersonalDTO;
import com.spring.ApiSystem.personal.dto.response.ResListarPersonaisDTO;
import com.spring.ApiSystem.usuario.Usuario;
import com.spring.ApiSystem.usuario.dto.request.ReqCadastroUsuarioDTO;
import com.spring.ApiSystem.usuario.dto.response.ResCadastrarUsuarioDTO;
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
@RequestMapping("/personais")
public class PersonalController {

    private final PersonalService personalService;
    private final JpaUserDetailsService userDetails;
    private final FilterService filterService;

    public PersonalController(PersonalService personalService, JpaUserDetailsService userDetails, FilterService filterService) {
        this.personalService = personalService;
        this.userDetails = userDetails;
        this.filterService = filterService;
    }

    @Operation(summary = "Criar usuário", description = "Endpoint para cadastro de usuários no sistema")
    @PostMapping("/cadastro")
    public ResponseEntity<ResCadastrarPersonalDTO> cadastrarUsuario(@Valid @RequestBody ReqCadastroPersonalDTO cadastroUsuarioDTO) {
        return ResponseEntity.ok(personalService.cadastrarUsuario(cadastroUsuarioDTO));

    }

    @PutMapping("/{personalId}/buffer")
    public ResponseEntity<Void> atualizarBufferMinutos(
            @PathVariable Long personalId,
            @Valid @RequestBody ReqAtualizarBufferDTO request) {

        personalService.atualizarBufferMinutos(personalId, request.bufferMinutos());
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Listar alunos (necessário login)",
            description = "Endpoint para listar alunos no sistema")
    @GetMapping
    public ResponseEntity<List<ResListarPersonaisDTO>> listarPersonais(@PageableDefault(sort = "nome")
                                                                 Pageable pageable) {
        return ResponseEntity.ok(personalService.listarPersonais(pageable));
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


    @Operation(summary = "Editar personal (necessário login)",
            description = "Endpoint para a edição de dados de personal no sistema")
    @PutMapping("/me")
    public ResponseEntity<ResAtualizarPersonalDTO> atualizarPersonal(@Valid @RequestBody ReqAtualizarPersonalDTO dto,
                                                                     HttpServletResponse response) {
        Personal usuario = userDetails.getCurrentPersonal();
        ResAtualizarPersonalDTO usuarioEditado = personalService.atualizarUsuario(dto, usuario);

        if(usuarioEditado == null){
            return ResponseEntity.notFound().build();
        }

        filterService.removerCookie(response);
        filterService.gerarCookie(response, usuarioEditado.email());
        return ResponseEntity.ok(usuarioEditado);
    }
}
