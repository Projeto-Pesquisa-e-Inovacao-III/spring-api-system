package com.spring.ApiSystem.domain.nocodetool;

import com.spring.ApiSystem.domain.nocodetool.dto.request.ReqCriarNoCodeDTO;
import com.spring.ApiSystem.domain.nocodetool.dto.response.ResBuscarNoCodeDTO;
import com.spring.ApiSystem.domain.personal.Personal;
import com.spring.ApiSystem.domain.usuario.security.JpaUserDetailsService;
import com.spring.ApiSystem.domain.usuario.ImageStorageService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class NoCodeServiceTest {

    private NoCodeRepository noCodeRepository;
    private NoCodeImageRepository noCodeImageRepository;
    private JpaUserDetailsService detailsService;
    private ImageStorageService imageStorageService;
    private NoCodeService noCodeService;
    private Personal testPersonal;

    @BeforeEach
    void setUp() {
        noCodeRepository = mock(NoCodeRepository.class);
        noCodeImageRepository = mock(NoCodeImageRepository.class);
        detailsService = mock(JpaUserDetailsService.class);
        imageStorageService = mock(ImageStorageService.class);
        noCodeService = new NoCodeService(noCodeRepository, noCodeImageRepository, detailsService, imageStorageService);

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

    @Test
    void saveImage_ShouldReturnUrlAndSaveNoCodeImage() throws java.io.IOException {
        org.springframework.web.multipart.MultipartFile image = mock(org.springframework.web.multipart.MultipartFile.class);
        String section = "header";
        String expectedUrl = "http://storage.com/image.png";

        when(imageStorageService.salvarBlob(image)).thenReturn(expectedUrl);
        when(noCodeRepository.findFirstByUserIdOrderByCreatedAtDesc(1L)).thenReturn(new NoCode());

        String result = noCodeService.saveImage(image, section);

        assertEquals(expectedUrl, result);
        verify(imageStorageService).salvarBlob(image);
        verify(noCodeImageRepository).save(any(NoCodeImage.class));
    }
}
