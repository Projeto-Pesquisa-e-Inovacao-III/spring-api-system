package com.spring.ApiSystem.domain.nocodetool;

import com.spring.ApiSystem.domain.nocodetool.dto.request.ReqCriarNoCodeDTO;
import com.spring.ApiSystem.domain.nocodetool.dto.response.ResBuscarNoCodeDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.spring.ApiSystem.domain.nocodetool.dto.request.ReqAtualizarNoCodeDTO;
import com.spring.ApiSystem.domain.nocodetool.dto.request.ReqRenomearNoCodeDTO;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.UUID;

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

    @PostMapping("/restore/{id}")
    public ResponseEntity<ReqCriarNoCodeDTO> restoreContent(@PathVariable UUID id) {
        ReqCriarNoCodeDTO req = noCodeService.restoreContent(id);
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

    @GetMapping("/history")
    public ResponseEntity<Page<ResBuscarNoCodeDTO>> getContentHistory(Pageable pageable) {
        Page<ResBuscarNoCodeDTO> page = noCodeService.getContentHistory(pageable);

        if (page.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(page);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteContent(@PathVariable UUID id) {
        noCodeService.deleteContent(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ResBuscarNoCodeDTO> renameContent(@PathVariable UUID id, @RequestBody ReqRenomearNoCodeDTO req) {
        ResBuscarNoCodeDTO res = noCodeService.renameContent(id, req);
        return ResponseEntity.ok(res);
    }
}
