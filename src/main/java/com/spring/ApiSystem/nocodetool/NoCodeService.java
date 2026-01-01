package com.spring.ApiSystem.nocodetool;

import com.spring.ApiSystem.nocodetool.dto.NoCodeDTO;
import com.spring.ApiSystem.nocodetool.exception.NoCodeContentNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Service
public class NoCodeService {
    private final NoCodeRepository noCodeRepository;

    public NoCodeService(NoCodeRepository noCodeRepository) {
        this.noCodeRepository = noCodeRepository;
    }

    @Transactional
    public NoCodeDTO updateContent(NoCodeDTO noCodeDTO) {
        UUID id = noCodeDTO.getId();

        NoCode content = noCodeRepository.getReferenceById(id);

        content.setContent(noCodeDTO.getContent());
        content.setElementTag(noCodeDTO.getElementTag());

        content = noCodeRepository.save(content);
        return new NoCodeDTO(content);
    }
}
