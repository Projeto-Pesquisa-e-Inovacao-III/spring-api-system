package com.spring.ApiSystem.shared.infrastructure.telemetry;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/logs/mobile")
@ConditionalOnProperty(prefix = "observability", name = "enabled", havingValue = "true", matchIfMissing = true)
public class MobileLogIngestionController {

    private final MobileLogIngestionService ingestionService;

    public MobileLogIngestionController(MobileLogIngestionService ingestionService) {
        this.ingestionService = ingestionService;
    }

    @PostMapping
    public ResponseEntity<Map<String, Object>> ingestLogs(
            @RequestBody(required = false) MobileLogBatchDTO batch,
            HttpServletRequest request
    ) {
        int ingestedCount = ingestionService.ingest(batch, request);
        return ResponseEntity.status(HttpStatus.ACCEPTED)
                .body(Map.of("status", "accepted", "ingested", ingestedCount));
    }
}
