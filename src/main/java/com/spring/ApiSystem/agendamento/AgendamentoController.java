package com.spring.ApiSystem.agendamento;

import com.spring.ApiSystem.agendamento.dto.request.ReqCriarAgendamentoDTO;
import com.spring.ApiSystem.agendamento.mapper.AgendamentoMapper;
import com.spring.ApiSystem.usuario.Usuario;
import com.spring.ApiSystem.usuario.UsuarioService;
import com.spring.ApiSystem.usuario.enums.TipoUsuario;
import com.spring.ApiSystem.usuario.exception.UsuarioNaoEncontradoException;
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
    public ResponseEntity<?> criarAgendamento(@RequestBody ReqCriarAgendamentoDTO reqCriarAgendamentoDTO,
                                              @AuthenticationPrincipal UserDetails userDetails) {
        String email = userDetails.getUsername();
        return ResponseEntity.ok(
                agendamentoService.criarAgendamento(reqCriarAgendamentoDTO, email)
        );
    }

    @GetMapping("/me")
    public ResponseEntity<?> buscarAgendamentosPorUsuario(@AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(
                agendamentoService.buscarAgendamentosPorUsuario(userDetails.getUsername())
        );
    }

}

