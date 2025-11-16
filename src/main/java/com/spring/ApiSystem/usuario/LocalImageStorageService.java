package com.spring.ApiSystem.usuario;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class LocalImageStorageService {
    private static final long MAX_IMAGE_SIZE = 5L * 1024 * 1024;

    @Value("${storage.local-dir:}")
    private String localDir;

    public String salvarBlob(MultipartFile imagem) throws IOException {
        validarImagem(imagem);
        Path path = salvarImagemLocal(imagem);
        return path.toString();
    }

    public Path salvarImagemLocal(MultipartFile imagem) throws IOException {

        Files.createDirectories(Paths.get(localDir));

        String nomeArquivo = System.currentTimeMillis() + "_" + imagem.getOriginalFilename();
        Path caminho = Paths.get(localDir, nomeArquivo);

        Files.write(caminho, imagem.getBytes());
        return caminho;
    }

    public String trocarImagem(MultipartFile imagem, Path path) throws IOException {
        deletarImagem(path);
        String newPath = salvarBlob(imagem);

        return newPath;
    }

    public void deletarImagem(Path path) throws IOException {
        Files.deleteIfExists(path);
    }

    public Resource buscarImagem(String nomeArquivo) throws IOException {
        Path caminho = Paths.get(localDir, nomeArquivo);

        if (!Files.exists(caminho)) {
            throw new FileNotFoundException("Imagem não encontrada: " + nomeArquivo);
        }

        UrlResource resource = new UrlResource(caminho.toUri());
        if (resource.exists() && resource.isReadable()) {
            return resource;
        } else {
            throw new IOException("Não foi possível ler a imagem");
        }
    }

    private void validarImagem(MultipartFile imagem) {
        if (imagem == null || imagem.isEmpty()) {
            throw new IllegalArgumentException("Imagem não pode ser nula ou vazia");
        }

        String contentType = imagem.getContentType();
        if (contentType == null || !contentType.startsWith("image/")) {
            throw new IllegalArgumentException("Arquivo não é uma imagem");
        }

        if (imagem.getSize() > MAX_IMAGE_SIZE) {
            throw new IllegalArgumentException("Imagem muito grande (máx 5MB)");
        }
    }
}
