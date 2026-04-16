package com.spring.ApiSystem.domain.nocodetool;

import com.spring.ApiSystem.domain.nocodetool.dto.request.ReqCriarNoCodeDTO;
import com.spring.ApiSystem.domain.nocodetool.dto.response.ResBuscarNoCodeDTO;
import com.spring.ApiSystem.domain.personal.Personal;
import com.spring.ApiSystem.domain.usuario.security.JpaUserDetailsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class NoCodeServiceTest {

    private NoCodeRepository noCodeRepository;
    private JpaUserDetailsService detailsService;
    private NoCodeService noCodeService;
    private Personal testPersonal;

    @BeforeEach
    void setUp() {
        noCodeRepository = mock(NoCodeRepository.class);
        detailsService = mock(JpaUserDetailsService.class);
        noCodeService = new NoCodeService(noCodeRepository, detailsService);

        testPersonal = new Personal();
        testPersonal.setId(1L);
        when(detailsService.getCurrentPersonal()).thenReturn(testPersonal);
    }

    @Test
    void createContent_ShouldSaveNewRecord() {
        ReqCriarNoCodeDTO req = new ReqCriarNoCodeDTO(null, "Name", "Desc", "{}", null);
        
        when(noCodeRepository.save(any(NoCode.class))).thenAnswer(invocation -> invocation.getArgument(0));

        ReqCriarNoCodeDTO result = noCodeService.createContent(req);

        assertNotNull(result);
        verify(noCodeRepository, times(1)).save(any(NoCode.class));
    }

    @Test
    void getContent_ShouldReturnLatestRecord() {
        NoCode latest = new NoCode();
        latest.setModificationName("Latest");
        latest.setContent("{\"v\": 2}");

        when(noCodeRepository.findFirstByUserIdOrderByCreatedAtDesc(1L)).thenReturn(latest);

        ResBuscarNoCodeDTO result = noCodeService.getContent();

        assertNotNull(result);
        assertEquals("{\"v\": 2}", result.content());
        verify(noCodeRepository).findFirstByUserIdOrderByCreatedAtDesc(1L);
    }
}
