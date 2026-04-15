package com.spring.ApiSystem.domain.nocodetool;

import com.spring.ApiSystem.domain.nocodetool.dto.request.ReqCriarNoCodeDTO;
import com.spring.ApiSystem.domain.nocodetool.dto.response.ResBuscarNoCodeDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.spring.ApiSystem.domain.nocodetool.dto.request.ReqAtualizarNoCodeDTO;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/api/no-code")
public class NoCodeController {
    private final NoCodeService noCodeService;

    public NoCodeController(NoCodeService noCodeService) {
        this.noCodeService = noCodeService;
    }

    @PostMapping
    public ResponseEntity<ReqCriarNoCodeDTO> createContent(@RequestBody ReqCriarNoCodeDTO req) {
        req = noCodeService.createContent(req);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(req.id()).toUri();
        return ResponseEntity.created(uri).body(req);
    }

    @PutMapping
    public ResponseEntity<ReqAtualizarNoCodeDTO> updateContent(@RequestBody ReqAtualizarNoCodeDTO req) {
        req = noCodeService.updateContent(req);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(req.id()).toUri();
        return ResponseEntity.created(uri).body(req);
    }

    @GetMapping
    public ResponseEntity<ResBuscarNoCodeDTO> getContent() {
        ResBuscarNoCodeDTO req = noCodeService.getContent();
        if (req == null) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(req);
    }
}
