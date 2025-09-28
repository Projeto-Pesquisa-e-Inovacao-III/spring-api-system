package com.spring.ApiSystem.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
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

    public String gerarToken(String email){
        try{
            Algorithm algorithm = Algorithm.RSA256(null,
                                                   pemService.getPrivateKey());

            return JWT.create()
                    .withIssuer("spring-api")
                    .withSubject(email)
                    .withExpiresAt(gerarDataExpiracao())
                    .sign(algorithm);
        }catch (Exception exception){
            throw new RuntimeException("Erro ao gerar o token: ", exception);
        }
    }

    public String validarToken(String token){
        try{
            Algorithm algorithm = Algorithm.RSA256(pemService.getPublicKey(),
                                          null);

            return JWT.require(algorithm)
                    .withIssuer("spring-api")
                    .build()
                    .verify(token)
                    .getSubject();

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
