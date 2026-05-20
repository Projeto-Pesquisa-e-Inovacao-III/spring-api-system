package com.spring.ApiSystem.shared.service;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Path;

public interface ImageStorageService {
    String salvarBlob(MultipartFile imagem) throws IOException;
    void deletarImagem(String path) throws IOException;
    Resource buscarImagem(String nomeArquivo) throws IOException;
    String trocarImagem(MultipartFile imagem, String path) throws IOException;
}
