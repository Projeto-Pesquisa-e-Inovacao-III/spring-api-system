package com.spring.ApiSystem.nocodetool;

import com.spring.ApiSystem.nocodetool.dto.NoCodeDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/no-code")
public class NoCodeController {
    private final NoCodeService noCodeService;

    public NoCodeController(NoCodeService noCodeService) {
        this.noCodeService = noCodeService;
    }

    @PutMapping
    public ResponseEntity<NoCodeDTO> updateContent(@RequestBody NoCodeDTO noCodeDTO) {
        noCodeDTO = noCodeService.updateContent(noCodeDTO);

        return ResponseEntity.ok(noCodeDTO);
    }
}
