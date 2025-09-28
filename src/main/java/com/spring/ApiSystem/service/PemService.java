package com.spring.ApiSystem.service;

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
    // Referenciado as chaves pública e privada

    @Value("${jwt.public.key}")
    private Resource publicKeyPem;

    @Value("${jwt.private.key}")
    private Resource privateKeyPem;

    // Funções para realização da leitura de cada chave

    public RSAPublicKey getPublicKey() throws Exception {
        String pem = new String(publicKeyPem.getInputStream().readAllBytes(), StandardCharsets.UTF_8);
        String cleanPem = pem
                .replace("-----BEGIN PUBLIC KEY-----", "")
                .replace("-----END PUBLIC KEY-----", "")
                .replaceAll("\\s", "");

        byte[] decoded = Base64.getDecoder().decode(cleanPem);
        X509EncodedKeySpec spec = new X509EncodedKeySpec(decoded);
        return (RSAPublicKey) KeyFactory.getInstance("RSA").generatePublic(spec);
    }

    public RSAPrivateKey getPrivateKey() throws Exception {
        String pem = new String(privateKeyPem.getInputStream().readAllBytes(), StandardCharsets.UTF_8);
        String cleanPem = pem
                .replace("-----BEGIN PRIVATE KEY-----", "")
                .replace("-----END PRIVATE KEY-----", "")
                .replaceAll("\\s", "");

        byte[] decoded = Base64.getDecoder().decode(cleanPem);
        PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(decoded);
        return (RSAPrivateKey) KeyFactory.getInstance("RSA").generatePrivate(spec);
    }
}
