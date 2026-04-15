package com.spring.ApiSystem.domain.nocodetool;

import com.spring.ApiSystem.domain.nocodetool.dto.request.ReqAtualizarNoCodeDTO;
import com.spring.ApiSystem.domain.nocodetool.dto.request.ReqCriarNoCodeDTO;
import com.spring.ApiSystem.domain.nocodetool.dto.response.ResBuscarNoCodeDTO;
import com.spring.ApiSystem.domain.personal.Personal;
import com.spring.ApiSystem.domain.usuario.security.JpaUserDetailsService;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@EnableJpaAuditing
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

        content.setContent(req.content());
        content.setUser(currentPersonal);

        content = noCodeRepository.save(content);

        return new ReqCriarNoCodeDTO(content);
    }

    @Transactional
    public ReqAtualizarNoCodeDTO updateContent(ReqAtualizarNoCodeDTO req) {
        Personal currentPersonal = detailsService.getCurrentPersonal();

        NoCode content = noCodeRepository.findByUserId(currentPersonal.getId());

        content.setContent(req.content());

        content = noCodeRepository.save(content);

        return new ReqAtualizarNoCodeDTO(content);
    }

    @Transactional
    public ResBuscarNoCodeDTO getContent() {
        Personal currentPersonal = detailsService.getCurrentPersonal();

        NoCode content = noCodeRepository.findByUserId(currentPersonal.getId());
        
        if (content == null) {
            return null;
        }
        
        return new ResBuscarNoCodeDTO(content);
    }
}
