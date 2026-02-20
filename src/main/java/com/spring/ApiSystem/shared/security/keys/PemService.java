package com.spring.ApiSystem.security.keys;

import com.spring.ApiSystem.exception.PemNaoEncontradaException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

@Service
public class PemService {

    @Value("${jwt.public.key:}")
    private Resource publicKeyPem;

    @Value("${jwt.private.key:}")
    private Resource privateKeyPem;

    public RSAPublicKey getPublicKey() {
        if (publicKeyPem == null || !publicKeyPem.exists()) {
            throw new PemNaoEncontradaException("Chave pública JWT não encontrada. Configure jwt.public.key no application.yml ou properties.");
        }

        try {
            String pem = new String(publicKeyPem.getInputStream().readAllBytes(), StandardCharsets.UTF_8);
            String cleanPem = pem
                    .replace("-----BEGIN PUBLIC KEY-----", "")
                    .replace("-----END PUBLIC KEY-----", "")
                    .replaceAll("\\s", "");

            byte[] decoded = Base64.getDecoder().decode(cleanPem);
            X509EncodedKeySpec spec = new X509EncodedKeySpec(decoded);
            return (RSAPublicKey) KeyFactory.getInstance("RSA").generatePublic(spec);
        } catch (Exception e) {
            throw new PemNaoEncontradaException("Erro ao ler a chave pública JWT: " + e.getMessage());
        }
    }

    public RSAPrivateKey getPrivateKey() {
        if (privateKeyPem == null || !privateKeyPem.exists()) {
            throw new PemNaoEncontradaException("Chave privada JWT não encontrada. Configure jwt.private.key no application.yml ou properties.");
        }

        try {
            String pem = new String(privateKeyPem.getInputStream().readAllBytes(), StandardCharsets.UTF_8);
            String cleanPem = pem
                    .replace("-----BEGIN PRIVATE KEY-----", "")
                    .replace("-----END PRIVATE KEY-----", "")
                    .replaceAll("\\s", "");

            byte[] decoded = Base64.getDecoder().decode(cleanPem);
            PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(decoded);
            return (RSAPrivateKey) KeyFactory.getInstance("RSA").generatePrivate(spec);
        } catch (Exception e) {
            throw new PemNaoEncontradaException("Erro ao ler a chave privada JWT: " + e.getMessage());
        }
    }
}
