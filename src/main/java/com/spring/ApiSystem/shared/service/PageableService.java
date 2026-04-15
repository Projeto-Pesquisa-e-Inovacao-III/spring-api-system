package com.spring.ApiSystem.shared.service;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class PageableService {
    public Pageable createPageableWithSetSize(Pageable pageable, int size) {
        return PageRequest.of(pageable.getPageNumber(), size, pageable.getSort());
    }

    public Pageable setMaxSizePageable(Pageable pageable, int maxSize) {
        if (pageable.getPageSize() > maxSize) {
            throw new IllegalArgumentException("O tamanho da página não pode ser maior que " + maxSize);
        }
        int size = Math.min(pageable.getPageSize(), maxSize);
        return PageRequest.of(pageable.getPageNumber(), size, pageable.getSort());
    }
}
