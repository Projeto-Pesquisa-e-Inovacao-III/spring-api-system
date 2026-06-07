package com.spring.ApiSystem.domain.nocodetool;

import com.spring.ApiSystem.domain.personal.Personal;
import com.spring.ApiSystem.domain.nocodetool.dto.request.ReqAtualizarNoCodeDTO;
import com.spring.ApiSystem.domain.nocodetool.dto.request.ReqCriarNoCodeDTO;
import com.spring.ApiSystem.domain.nocodetool.dto.request.ReqRenomearNoCodeDTO;
import com.spring.ApiSystem.domain.nocodetool.dto.response.ResBuscarNoCodeDTO;
import com.spring.ApiSystem.domain.nocodetool.mapper.NoCodeMapper;
import com.spring.ApiSystem.domain.personal.Personal;
import com.spring.ApiSystem.domain.usuario.security.JpaUserDetailsService;
import com.spring.ApiSystem.domain.usuario.ImageStorageService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class NoCodeService {
    private final NoCodeRepository noCodeRepository;
    private final NoCodeImageRepository noCodeImageRepository;
    private final JpaUserDetailsService detailsService;
    private final ImageStorageService imageStorageService;
    private final NoCodeMapper noCodeMapper;

    public NoCodeService(NoCodeRepository noCodeRepository, NoCodeImageRepository noCodeImageRepository, JpaUserDetailsService detailsService, ImageStorageService imageStorageService, NoCodeMapper noCodeMapper) {
        this.noCodeRepository = noCodeRepository;
        this.noCodeImageRepository = noCodeImageRepository;
        this.detailsService = detailsService;
        this.imageStorageService = imageStorageService;
        this.noCodeMapper = noCodeMapper;
    }

    @Transactional
    public String saveImage(MultipartFile image, String section) throws IOException {
        Personal currentPersonal = detailsService.getCurrentPersonal();
        NoCode noCode = noCodeRepository.findFirstByUserIdOrderByCreatedAtDesc(currentPersonal.getId());

        String url = imageStorageService.salvarBlob(image);

        if (noCode != null) {
            NoCodeImage noCodeImage = new NoCodeImage(url, section, noCode);
            noCodeImageRepository.save(noCodeImage);
        }

        return url;
    }

    @Transactional
    @CacheEvict(value = "noCode", key = "'current'")
    public ReqCriarNoCodeDTO createContent(ReqCriarNoCodeDTO req) {
        NoCode content = noCodeMapper.toEntity(req);

        Personal currentPersonal = detailsService.getCurrentPersonal();

        content.setContent(req.content());
        content.setModificationName(req.modificationName());
        content.setDescription(req.description());
        content.setUser(currentPersonal.getUsuario());

        content = noCodeRepository.save(content);

        return noCodeMapper.toReqCriarNoCodeDTO(content);
    }

    @Transactional
    @CacheEvict(cacheNames = "noCode", key = "'current'")
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

        return noCodeMapper.toReqCriarNoCodeDTO(restored);
    }

    @Transactional
    @CacheEvict(cacheNames = "noCode", key = "'current'")
    public ReqAtualizarNoCodeDTO updateContent(ReqAtualizarNoCodeDTO req) {
        NoCode content;
        if (req.id() != null) {
            content = noCodeRepository.findById(req.id())
                    .orElseThrow(() -> new EntityNotFoundException("Conteúdo não encontrado com ID: " + req.id()));
        } else {
            Personal currentPersonal = detailsService.getCurrentPersonal();
            content = noCodeRepository.findFirstByUserIdOrderByCreatedAtDesc(currentPersonal.getId());
            if (content == null) {
                throw new EntityNotFoundException("Nenhum conteúdo NoCode encontrado para o usuário atual.");
            }
        }

        content.setContent(req.content());
        content.setModificationName(req.modificationName());
        content.setDescription(req.description());

        content = noCodeRepository.save(content);

        return noCodeMapper.toReqAtualizarNoCodeDTO(content);
    }

    @Transactional(readOnly = true)
    @Cacheable(cacheNames = "noCode", key = "'current'", unless = "#result == null")
    public ResBuscarNoCodeDTO getContent() {
        NoCode content = noCodeRepository.findFirstByOrderByCreatedAtDesc();

        if (content == null) {
            return null;
        }

        return noCodeMapper.toResBuscarNoCodeDTO(content);
    }

    @Transactional
    public Page<ResBuscarNoCodeDTO> getContentHistory(Pageable pageable) {
        Personal currentPersonal = detailsService.getCurrentPersonal();

        Page<NoCode> contentPage = noCodeRepository.findAllByUserIdOrderByCreatedAtDesc(currentPersonal.getId(), pageable);
        
        if (contentPage == null) {
            return null;
        }

        return contentPage.map(noCodeMapper::toResBuscarNoCodeDTO);
    }

    @Transactional
    @CacheEvict(cacheNames = "noCode", key = "'current'")
    public void deleteContent(UUID id) {
        NoCode content = noCodeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Conteúdo não encontrado: " + id));
        noCodeRepository.delete(content);
    }

    @Transactional
    @CacheEvict(cacheNames = "noCode", key = "'current'")
    public ResBuscarNoCodeDTO renameContent(UUID id, ReqRenomearNoCodeDTO req) {
        NoCode content = noCodeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Conteúdo não encontrado: " + id));

        noCodeMapper.updateNoCodeFromRenameDto(req, content);
        content = noCodeRepository.save(content);

        return noCodeMapper.toResBuscarNoCodeDTO(content);
    }
}
