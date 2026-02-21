package com.spring.ApiSystem.shared.security.token;

import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;

import com.spring.ApiSystem.shared.security.keys.PemService;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.stereotype.Service;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Service
public class TokenService {

    private final PemService pemService;

    public TokenService(PemService pemService) {
        this.pemService = pemService;
    }

    public JwtEncoder jwtEncoder() throws Exception {
        RSAKey rsaKey = new RSAKey.Builder(pemService.getPublicKey())
                .privateKey(pemService.getPrivateKey())
                .build();
        JWKSource<SecurityContext> jwkSource = new ImmutableJWKSet<>(new JWKSet(rsaKey));
        return new NimbusJwtEncoder(jwkSource);
    }

    public JwtDecoder jwtDecoder() throws Exception {
        return NimbusJwtDecoder.withPublicKey(pemService.getPublicKey()).build();
    }

    public String gerarToken(String email){
        try{
            JwtClaimsSet token = JwtClaimsSet.builder()
                    .issuer("spring-api")
                    .subject(email)
                    .issuedAt(Instant.now())
                    .expiresAt(gerarDataExpiracao())
                    .build();

            return jwtEncoder().encode(JwtEncoderParameters.from(token)).getTokenValue();
        }catch (Exception exception){
            System.out.println( exception.getMessage() );
            throw new RuntimeException("Erro ao gerar o token: ", exception);
        }
    }

    public String subjectToken(String token){
        try{
            Jwt jwt = jwtDecoder().decode(token);
            return jwt.getSubject();
        }catch (Exception exception){
            throw new RuntimeException("Token inválido: ", exception);
        }
    }

    /*
    Transforma em Tempo Universal Coordenado (UTC)
    para que seja gerado um tempo de expiração correto
    independente da localidade
    */
    private Instant gerarDataExpiracao(){
        return LocalDateTime.now()
                .plusHours(1)
                .toInstant(ZoneOffset.of("-03:00"));
    }
}
