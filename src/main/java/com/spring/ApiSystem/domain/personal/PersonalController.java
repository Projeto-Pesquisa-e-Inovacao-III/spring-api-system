package com.spring.ApiSystem.domain.personal;

import com.spring.ApiSystem.domain.personal.dto.request.ReqAtualizarBufferDTO;
import com.spring.ApiSystem.domain.personal.dto.request.ReqAtualizarPersonalDTO;
import com.spring.ApiSystem.domain.admin.dto.request.ReqCadastroPersonalDTO;
import com.spring.ApiSystem.domain.personal.dto.response.ResAtualizarPersonalDTO;
import com.spring.ApiSystem.domain.personal.dto.response.ResBuscarBufferDTO;
import com.spring.ApiSystem.domain.personal.dto.response.ResBuscarPersonalPorIdDTO;
import com.spring.ApiSystem.domain.admin.dto.response.ResCadastrarPersonalDTO;
import com.spring.ApiSystem.domain.personal.dto.response.ResListarPersonaisDTO;
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

@RestController
@RequestMapping("/api/personais")
public class PersonalController {

    private final PersonalService personalService;
    private final JpaUserDetailsService userDetails;
    private final FilterService filterService;

    public PersonalController(
            PersonalService personalService,
            JpaUserDetailsService userDetails,
            FilterService filterService
    ) {
        this.personalService = personalService;
        this.userDetails = userDetails;
        this.filterService = filterService;
    }

    @PutMapping("/me/buffer")
    public ResponseEntity<Void> atualizarBufferMinutos(
            @Valid @RequestBody ReqAtualizarBufferDTO request
    ) {
        Personal personal = userDetails.getCurrentPersonal();
        personalService.atualizarBufferMinutos(personal.getId(), request.bufferMinutos());
        return ResponseEntity.noContent().build();
    }

    @Operation(
            summary = "Buscar buffer do personal (necessário login)",
            description = "Endpoint para buscar o buffer de minutos de um personal específico"
    )
    @GetMapping("/me/buffer")
    public ResponseEntity<ResBuscarBufferDTO> buscarBuffer() {
        return ResponseEntity.ok(personalService.buscarBuffer());
    }

    @Operation(
            summary = "Listar personais (necessário login)",
            description = "Endpoint para listar personais no sistema"
    )
    @GetMapping
    public ResponseEntity<Page<ResListarPersonaisDTO>> listarPersonais(
            @SortDefault.SortDefaults({
                    @SortDefault(sort = "usuario.nome", direction = Sort.Direction.ASC),
                    @SortDefault(sort = "id", direction = Sort.Direction.ASC)
            }) Pageable pageable,
            @RequestParam(required = false) String nome
    ) {
        Page<ResListarPersonaisDTO> personals = personalService.listarPersonais(pageable, nome);

        if(personals.isEmpty()){
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(personals);
    }

    @Operation(
            summary = "Buscar personal por ID (necessário login)",
            description = "Endpoint para buscar um personal específico pelo ID no sistema"
    )
    @GetMapping("/{id}")
    public ResponseEntity<ResBuscarPersonalPorIdDTO> buscarPersonalPorId(@PathVariable Long id) {
        return ResponseEntity.ok(personalService.buscarPersonalPorId(id));
    }

    @Operation(
            summary = "Editar personal (necessário login)",
            description = "Endpoint para a edição de dados de personal no sistema"
    )
    @PutMapping("/me")
    public ResponseEntity<ResAtualizarPersonalDTO> atualizarPersonal(
            @Valid @RequestBody ReqAtualizarPersonalDTO dto,
            HttpServletResponse response
    ) {
        Personal personal = userDetails.getCurrentPersonal();
        ResAtualizarPersonalDTO usuarioEditado = personalService.atualizarUsuario(dto, personal);

        filterService.removerCookie(response);
        filterService.gerarCookie(response, usuarioEditado.email());

        return ResponseEntity.ok(usuarioEditado);
    }
}