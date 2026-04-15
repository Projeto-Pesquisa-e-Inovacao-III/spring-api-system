package com.spring.ApiSystem.domain.nocodetool;

import com.spring.ApiSystem.domain.nocodetool.dto.ReqAtualizarNoCodeDTO;
import com.spring.ApiSystem.domain.nocodetool.dto.ReqCriarNoCodeDTO;
import com.spring.ApiSystem.domain.personal.Personal;
import com.spring.ApiSystem.domain.usuario.security.JpaUserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
public class NoCodeService {
    private final NoCodeRepository noCodeRepository;
    private final JpaUserDetailsService detailsService;

    public NoCodeService(NoCodeRepository noCodeRepository, JpaUserDetailsService detailsService) {
        this.noCodeRepository = noCodeRepository;
        this.detailsService = detailsService;
    }

    @Transactional
    public ReqCriarNoCodeDTO createContent(ReqCriarNoCodeDTO req) {
        NoCode content = new NoCode(req);

        Personal currentPersonal = detailsService.getCurrentPersonal();

        content.setCreatedAt(LocalDateTime.now());
        content.setUpdatedAt(LocalDateTime.now());
        content.setContent(req.content());
        content.setUser(currentPersonal);

        content = noCodeRepository.save(content);

        return new ReqCriarNoCodeDTO(content);
    }

    @Transactional
    public ReqAtualizarNoCodeDTO updateContent(ReqAtualizarNoCodeDTO req) {
        UUID id = req.id();

        NoCode content = noCodeRepository.getReferenceById(id);

        content.setContent(req.content());
        content.setUpdatedAt(LocalDateTime.now());

        content = noCodeRepository.save(content);

        return new ReqAtualizarNoCodeDTO(content);
    }
}
