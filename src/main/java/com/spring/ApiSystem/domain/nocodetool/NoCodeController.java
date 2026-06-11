package com.spring.ApiSystem.domain.nocodetool;

import com.spring.ApiSystem.domain.nocodetool.dto.request.ReqCriarNoCodeDTO;
import com.spring.ApiSystem.domain.nocodetool.dto.response.ResBuscarNoCodeDTO;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.spring.ApiSystem.domain.nocodetool.dto.request.ReqAtualizarNoCodeDTO;
import com.spring.ApiSystem.domain.nocodetool.dto.request.ReqRenomearNoCodeDTO;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.UUID;

import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping("/api/no-code")
public class NoCodeController {
    private final NoCodeService noCodeService;

    public NoCodeController(NoCodeService noCodeService) {
        this.noCodeService = noCodeService;
    }

    @PostMapping("/image")
    public ResponseEntity<Map<String, String>> uploadImage(
            @RequestParam("image") MultipartFile image,
            @RequestParam("section") String section
    ) throws IOException {
        String url = noCodeService.saveImage(image, section);
        return ResponseEntity.ok(Map.of("url", url));
    }

    @GetMapping("/images/**")
    public ResponseEntity<Resource> buscarImagem(HttpServletRequest request) throws IOException {
        String requestUri = request.getRequestURI();
        String storageKey = requestUri.split("/api/no-code/images/", 2)[1];
        Resource resource = noCodeService.buscarImagem(storageKey);
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(resource);
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

    //isso vai servir só pra atualizar imagem. talvez tenha um jeito melhormas é feriado
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
