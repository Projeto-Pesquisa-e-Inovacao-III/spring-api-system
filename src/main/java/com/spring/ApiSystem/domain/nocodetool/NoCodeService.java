package com.spring.ApiSystem.domain.nocodetool;

import com.spring.ApiSystem.domain.nocodetool.dto.request.ReqAtualizarNoCodeDTO;
import com.spring.ApiSystem.domain.nocodetool.dto.request.ReqCriarNoCodeDTO;
import com.spring.ApiSystem.domain.nocodetool.dto.request.ReqRenomearNoCodeDTO;
import com.spring.ApiSystem.domain.nocodetool.dto.response.ResBuscarNoCodeDTO;
import com.spring.ApiSystem.domain.personal.Personal;
import com.spring.ApiSystem.domain.usuario.security.JpaUserDetailsService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;
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
        content.setModificationName(req.modificationName());
        content.setDescription(req.description());
        content.setUser(currentPersonal.getUsuario());

        content = noCodeRepository.save(content);

        return new ReqCriarNoCodeDTO(content);
    }

    @Transactional
    public ReqCriarNoCodeDTO restoreContent(UUID id) {
        NoCode original = noCodeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Conteúdo não encontrado: " + id));
        Personal currentPersonal = detailsService.getCurrentPersonal();

        NoCode restored = new NoCode();

        restored.setContent(original.getContent());
        restored.setModificationName(original.getModificationName());
        restored.setDescription(original.getDescription());
        restored.setUser(currentPersonal.getUsuario());

        restored.setRestoredAt(LocalDateTime.now());
        restored.setRestoredFromId(original.getId());

        restored = noCodeRepository.save(restored);

        return new ReqCriarNoCodeDTO(restored);
    }

    @Transactional
    public ReqAtualizarNoCodeDTO updateContent(ReqAtualizarNoCodeDTO req) {
        Personal currentPersonal = detailsService.getCurrentPersonal();

        NoCode content = noCodeRepository.findFirstByUserIdOrderByCreatedAtDesc(currentPersonal.getId());

        content.setContent(req.content());
        content.setModificationName(req.modificationName());
        content.setDescription(req.description());

        content = noCodeRepository.save(content);

        return new ReqAtualizarNoCodeDTO(content);
    }

    @Transactional
    public ResBuscarNoCodeDTO getContent() {
        Personal currentPersonal = detailsService.getCurrentPersonal();

        NoCode content = noCodeRepository.findFirstByUserIdOrderByCreatedAtDesc(currentPersonal.getId());
        
        if (content == null) {
            return null;
        }
        
        return new ResBuscarNoCodeDTO(content);
    }

    @Transactional
    public Page<ResBuscarNoCodeDTO> getContentHistory(Pageable pageable) {
        Personal currentPersonal = detailsService.getCurrentPersonal();

        Page<NoCode> contentPage = noCodeRepository.findAllByUserIdOrderByCreatedAtDesc(currentPersonal.getId(), pageable);
        
        if (contentPage == null) {
            return null;
        }

        return contentPage.map(ResBuscarNoCodeDTO::new);
    }

    @Transactional
    public void deleteContent(UUID id) {
        NoCode content = noCodeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Conteúdo não encontrado: " + id));
        noCodeRepository.delete(content);
    }

    @Transactional
    public ResBuscarNoCodeDTO renameContent(UUID id, ReqRenomearNoCodeDTO req) {
        NoCode content = noCodeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Conteúdo não encontrado: " + id));

        content.setModificationName(req.modificationName());
        content = noCodeRepository.save(content);

        return new ResBuscarNoCodeDTO(content);
    }
}
