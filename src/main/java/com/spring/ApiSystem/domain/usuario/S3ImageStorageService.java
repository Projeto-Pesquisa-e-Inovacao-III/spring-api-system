package com.spring.ApiSystem.domain.usuario;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.unit.DataSize;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedGetObjectRequest;

import javax.imageio.ImageIO;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.util.Objects;

@Service
@ConditionalOnProperty(name = "storage.type", havingValue = "s3")
public class S3ImageStorageService implements ImageStorageService {

    @Value("${storage.s3.bucket:}")
    private String s3Bucket;

    @Value("${storage.s3.path:imagens/perfil/}")
    private String s3Path;

    @Value("${spring.servlet.multipart.max-file-size:4MB}")
    private DataSize maxImageSize;

    private final S3Client s3Client;
    private final S3Presigner s3Presigner;

    public S3ImageStorageService(S3Client s3Client, S3Presigner s3Presigner) {
        this.s3Client = s3Client;
        this.s3Presigner = s3Presigner;
    }

    @Override
    public String salvarBlob(MultipartFile imagem) throws IOException {
        validarImagem(imagem);
        garantirBucketConfigurado();

        String original = Objects.requireNonNullElse(imagem.getOriginalFilename(), "file");
        String safeName = sanitizarNomeArquivo(original);
        String nomeArquivo = System.currentTimeMillis() + "_" + safeName;
        String s3Key = construirChaveS3(nomeArquivo);

        PutObjectRequest putReq = PutObjectRequest.builder()
                .bucket(s3Bucket)
                .key(s3Key)
                .contentType(imagem.getContentType())
                .build();

        try (InputStream in = imagem.getInputStream()) {
            s3Client.putObject(putReq, RequestBody.fromInputStream(in, imagem.getSize()));
        }

        return s3Key;
    }

    @Override
    public String trocarImagem(MultipartFile imagem, Path currentPath) throws IOException {
        if (currentPath != null) {
            deletarImagem(currentPath);
        }
        return salvarBlob(imagem);
    }

    @Override
    public void deletarImagem(Path path) throws IOException {
        if (path == null) return;
        garantirBucketConfigurado();

        String key = path.toString();
        if (key == null || key.isBlank()) return;

        DeleteObjectRequest del = DeleteObjectRequest.builder()
                .bucket(s3Bucket)
                .key(key)
                .build();
        s3Client.deleteObject(del);
    }

    @Override
    public Resource buscarImagem(String fileName) throws IOException {
        garantirBucketConfigurado();

        GetObjectRequest getReq = GetObjectRequest.builder()
                .bucket(s3Bucket)
                .key(fileName)
                .build();

        GetObjectPresignRequest presignRequest = GetObjectPresignRequest.builder()
                .getObjectRequest(getReq)
                .signatureDuration(Duration.ofMinutes(15))
                .build();

        PresignedGetObjectRequest presigned = s3Presigner.presignGetObject(presignRequest);
        URL uri = presigned.url();
        return new UrlResource(uri);
    }

    private void validarImagem(MultipartFile imagem) {
        if (imagem == null || imagem.isEmpty()) {
            throw new IllegalArgumentException("Imagem não pode ser nula ou vazia");
        }

        if (maxImageSize != null && imagem.getSize() > maxImageSize.toBytes()) {
            throw new IllegalArgumentException("Imagem muito grande (máx " + maxImageSize.toMegabytes() + "MB)");
        }

        String contentType = imagem.getContentType();
        if (contentType == null || !contentType.startsWith("image/")) {
            throw new IllegalArgumentException("Arquivo não é uma imagem");
        }

        try (InputStream is = imagem.getInputStream()) {
            if (ImageIO.read(is) == null) {
                throw new IllegalArgumentException("O conteúdo do arquivo não é uma imagem válida ou está corrompido");
            }
        } catch (IOException e) {
            throw new IllegalArgumentException("Erro ao processar o arquivo de imagem", e);
        }

    }

    private void garantirBucketConfigurado() {
        if (s3Bucket == null || s3Bucket.isBlank()) {
            throw new IllegalStateException("Bucket S3 não configurado");
        }
    }

    private String sanitizarNomeArquivo(String original) {
        String name = Paths.get(original).getFileName().toString();
        return name.replaceAll("[^A-Za-z0-9._-]", "_");
    }

    private String normalizarCaminhoS3() {
        if (s3Path == null || s3Path.isEmpty()) {
            return "";
        }

        if (s3Path.endsWith("/")) {
            return s3Path;
        } else {
            return s3Path + "/";
        }
    }

    private String construirChaveS3(String filename) {
        return normalizarCaminhoS3() + filename;
    }
}
