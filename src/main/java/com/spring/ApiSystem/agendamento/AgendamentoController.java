package com.spring.ApiSystem.agendamento;

import com.spring.ApiSystem.agendamento.dto.request.CriarAgendamentoDTO;
import com.spring.ApiSystem.agendamento.dto.request.ReagendarAgendamentoDTO;
import com.spring.ApiSystem.agendamento.mapper.AgendamentoMapper;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import jakarta.validation.Valid;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Agendamentos", description = "Operações relacionadas a agendamentos")
@RestController
@RequestMapping("/agendamentos")
public class AgendamentoController {


    private final AgendamentoService agendamentoService;
    private final AgendamentoMapper agendamentoMapper;

    public AgendamentoController(AgendamentoService agendamentoService, AgendamentoMapper agendamentoMapper) {
        this.agendamentoService = agendamentoService;
        this.agendamentoMapper = agendamentoMapper;
    }

    @PostMapping
    public ResponseEntity<?> criarAgendamento(@Valid @RequestBody CriarAgendamentoDTO criarAgendamentoDTO){
        try {
            Agendamento agendamentoCriado = agendamentoService.criar(criarAgendamentoDTO);
            return new ResponseEntity<>("Agendamento realizado com sucesso.", HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>("Erro ao realizar agendamento: " + e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping
    public ResponseEntity<Page<?>> getAgendamentos(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Page<?> agendamentos = agendamentoService
                .pegarTodosAgendamentosDesseUsuario(userDetails.getUsername(), page, size);
        return ResponseEntity.ok(agendamentos);
    }

    @PatchMapping("/{id}/reagendar")
    public ResponseEntity<?> reagendarAgendamento(@PathVariable Long id  , @Valid @RequestBody ReagendarAgendamentoDTO reagendarAgendamentoDTO){
        try {
            Agendamento agendamentoReagendado = agendamentoService.reagendar( id , reagendarAgendamentoDTO);
            return new ResponseEntity<>("Agendamento reagendado com sucesso.", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Erro ao reagendar agendamento: " + e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PatchMapping("/{id}/aceitar")
    public ResponseEntity<?> aceitarAgendamento(@PathVariable Long id){
        try {
            Agendamento agendamentoAceito = agendamentoService.aceitaAgendamento(id);
            return new ResponseEntity<>("Agendamento aceito com sucesso.", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Erro ao aceitar agendamento: " + e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PatchMapping("/{id}/recusar")
    public ResponseEntity<?> recusArgendamento(@PathVariable Long id){
        try {
            Agendamento agendamentoAceito = agendamentoService.recusaAgendamento(id);
            return new ResponseEntity<>("Agendamento recusado com sucesso.", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Erro ao aceitar agendamento: " + e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> buscarAgendamentoPorId(@PathVariable Long id){
        try {
            return new ResponseEntity<>(  agendamentoService.buscarAgendamentoPorId(id), HttpStatus.OK);
        }catch (Exception e) {
            return new ResponseEntity<>("Erro ao buscar agendamento: " + e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}
