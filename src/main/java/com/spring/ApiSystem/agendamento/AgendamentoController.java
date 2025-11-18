package com.spring.ApiSystem.agendamento;

import com.spring.ApiSystem.agendamento.dto.request.ReqCadastrarAgendamentoDTO;
import com.spring.ApiSystem.usuario.UsuarioService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Agendamentos", description = "Operações relacionadas a agendamentos")
@RestController
@RequestMapping("/agendamentos")
public class AgendamentoController {


    private final AgendamentoService agendamentoService;
    private final UsuarioService usuarioService;

    public AgendamentoController(AgendamentoService agendamentoService, UsuarioService usuarioService) {
        this.agendamentoService = agendamentoService;
        this.usuarioService = usuarioService;
    }

    @PostMapping
    public ResponseEntity<?> criarAgendamento(@RequestBody ReqCadastrarAgendamentoDTO reqCadastrarAgendamentoDTO,
                                              @AuthenticationPrincipal UserDetails userDetails) {
        String email = userDetails.getUsername();
        return ResponseEntity.ok(
                agendamentoService.criarAgendamento(reqCadastrarAgendamentoDTO)
        );
    }

    @GetMapping("/me")
    public ResponseEntity<?> buscarAgendamentosPorUsuario(@AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(
                agendamentoService.buscarAgendamentosPorUsuario()
        );
    }

}

