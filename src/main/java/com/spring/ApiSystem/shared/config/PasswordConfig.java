package com.spring.ApiSystem.shared.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;

@Configuration
public class PasswordConfig {

    @Value("${argon.saltLength:16}")
    private int saltLength;

    @Value("${argon.hashLength:32}")
    private int hashLength;

    @Value("${argon.parallelism:1}")
    private int parallelism;

    @Value("${argon.memory:65536}")
    private int memory;

    @Value("${argon.iterations:3}")
    private int iterations;
;

    @Bean
    protected Argon2PasswordEncoder argon2PasswordEncoder() {
        return new Argon2PasswordEncoder(
                saltLength,
                hashLength,
                parallelism,
                memory,
                iterations
        );
    }
}
