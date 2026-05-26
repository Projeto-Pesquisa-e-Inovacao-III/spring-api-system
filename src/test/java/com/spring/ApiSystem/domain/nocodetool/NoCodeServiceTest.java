package com.spring.ApiSystem.domain.nocodetool;

import com.spring.ApiSystem.domain.nocodetool.dto.request.ReqCriarNoCodeDTO;
import com.spring.ApiSystem.domain.nocodetool.dto.response.ResBuscarNoCodeDTO;
import com.spring.ApiSystem.domain.nocodetool.mapper.NoCodeMapper;
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
    private NoCodeMapper noCodeMapper;
    private NoCodeService noCodeService;
    private Personal testPersonal;

    @BeforeEach
    void setUp() {
        noCodeRepository = mock(NoCodeRepository.class);
        noCodeImageRepository = mock(NoCodeImageRepository.class);
        detailsService = mock(JpaUserDetailsService.class);
        imageStorageService = mock(ImageStorageService.class);
        noCodeMapper = mock(NoCodeMapper.class);
        noCodeService = new NoCodeService(noCodeRepository, noCodeImageRepository, detailsService, imageStorageService, noCodeMapper);

        testPersonal = new Personal();
        testPersonal.setId(1L);
        when(detailsService.getCurrentPersonal()).thenReturn(testPersonal);
    }

    @Test
    void createContent_ShouldSaveNewRecord() {
        ReqCriarNoCodeDTO req = new ReqCriarNoCodeDTO(null, "Name", "Desc", "{}", null);
        NoCode entity = new NoCode();
        
        when(noCodeMapper.toEntity(req)).thenReturn(entity);
        when(noCodeRepository.save(any(NoCode.class))).thenReturn(entity);
        when(noCodeMapper.toReqCriarNoCodeDTO(entity)).thenReturn(req);

        ReqCriarNoCodeDTO result = noCodeService.createContent(req);

        assertNotNull(result);
        verify(noCodeRepository, times(1)).save(any(NoCode.class));
    }

    @Test
    void getContent_ShouldReturnLatestRecord() {
        NoCode latest = new NoCode();
        latest.setModificationName("Latest");
        latest.setContent("{\"v\": 2}");
        ResBuscarNoCodeDTO dto = new ResBuscarNoCodeDTO(null, "{\"v\": 2}", "Latest", null, null, null, null);

        when(noCodeRepository.findFirstByOrderByCreatedAtDesc()).thenReturn(latest);
        when(noCodeMapper.toResBuscarNoCodeDTO(latest)).thenReturn(dto);

        ResBuscarNoCodeDTO result = noCodeService.getContent();

        assertNotNull(result);
        assertEquals("{\"v\": 2}", result.content());
        verify(noCodeRepository).findFirstByOrderByCreatedAtDesc();
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
