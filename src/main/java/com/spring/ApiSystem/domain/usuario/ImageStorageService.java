package com.spring.ApiSystem.domain.usuario;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Path;

public interface ImageStorageService {
    String salvarBlob(MultipartFile imagem) throws IOException;
    String trocarImagem(MultipartFile imagem, Path currentFileName) throws IOException;
    void deletarImagem(Path path) throws IOException;
    Resource buscarImagem(String fileName) throws IOException;
}
