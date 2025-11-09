package com.spring.ApiSystem.config;

import com.spring.ApiSystem.config.filter.FilterService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.filter.CorsFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {
    private final FilterService filterService;
    private final CorsConfig corsConfig;

    public SecurityConfig(FilterService filterService, CorsConfig corsConfig){
        this.filterService = filterService;
        this.corsConfig = corsConfig;
    }

    @Value("${spring.profiles.active}")
    private String perfilAtivo;

    /*
    Configura como será o acesso aos endpoints aqui:
    - formulário padrão de login desabilitado
    - endpoints de login e cadastro liberado para todos
    - demais endpoints requerem o token
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .headers(headers -> headers
                        .frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin))
                .authorizeHttpRequests(auth -> {
                    auth.requestMatchers(HttpMethod.POST,
                                    "/usuarios/cadastro",
                                    "/usuarios/login"
                            ).permitAll()
                            .requestMatchers("/agendamentos/**").permitAll()
                            .requestMatchers("/checkouts/**").permitAll()
                            .requestMatchers("/produtos-contratados/**").permitAll();

                    if (perfilAtivo.equals("dev")) {
                        auth.requestMatchers(HttpMethod.GET,
                                        "/v3/api-docs/**",
                                        "/swagger-ui/**",
                                        "/doc"
                                ).permitAll()
                                .requestMatchers("/h2-console/**").permitAll();
                    }

                    auth.anyRequest().authenticated();
                })
                .addFilterBefore(new CorsFilter(corsConfig.corsConfigurationSource()),
                        UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(filterService, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Value("${argon.saltLength}")
    Integer saltLength;

    @Value("${argon.hashLength}")
    Integer hashLength;

    @Value("${argon.parallelism}")
    Integer parallelism;

    @Value("${argon.memory}")
    Integer memory;

    @Value("${argon.iterations}")
    Integer iterations;

    @Bean
    public Argon2PasswordEncoder argon2PasswordEncoder() {
        return new Argon2PasswordEncoder(saltLength,
                                        hashLength,
                                        parallelism,
                                        memory,
                                        iterations);
    }
}
