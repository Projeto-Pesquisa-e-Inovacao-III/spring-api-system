
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

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .headers(headers -> headers
                        .frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin))
                .authorizeHttpRequests(auth -> {
                    if ("dev".equals(perfilAtivo) || "docker".equals(perfilAtivo)) {
                        auth.requestMatchers(HttpMethod.GET,
                                "/v3/api-docs/**",
                                "/swagger-ui/**",
                                "/doc"
                        ).permitAll();
                        auth.requestMatchers("/h2-console/**").permitAll();
                    }
                    // 1. ROTAS PÚBLICAS
                    auth.requestMatchers(HttpMethod.OPTIONS, "/**").permitAll();
                    auth.requestMatchers(HttpMethod.GET, "/produtos-exibicoes/ativos").permitAll();
                    auth.requestMatchers(HttpMethod.POST,
                            "/alunos/cadastro",
                            "/personais/cadastro",
                            "/usuarios/login",
                            "/produtos-contratados/pagamento",
                            "/api/password-reset/**"
                    ).permitAll();
                    auth.requestMatchers(HttpMethod.GET,
                            "/produtos-exibicoes",
                            "/usuarios/auth"
                    ).permitAll();

                    // 2. ROTAS ESPECÍFICAS DO ALUNO (antes das genéricas)
                    auth.requestMatchers(HttpMethod.GET,
                            "/produtos-contratados/total-tipo/*",
                            "/personais"
                    ).hasRole("ALUNO");

                    // 3. ROTAS ESPECÍFICAS DO PERSONAL (antes das genéricas)
                    auth.requestMatchers(HttpMethod.GET,
                            "/alunos",
                            "/alunos/*"
                    ).hasRole("PERSONAL");

                    auth.requestMatchers(
                            "/agendamentos/*/confirmar-conclusao",
                            "/agendamentos/ausencia",
                            "/agendamentos/consultoria-realizadas/*",
                            "/agendamentos/contagem-status-data",
                            "/produtos-contratados/ganhos-mes/*",
                            "/produtos-contratados/planos-vendidos/*",
                            "/produtos-contratados/quantidade-e-percentual-alunos-expirados"
                    ).hasRole("PERSONAL");

                    // 4. ROTAS COMPARTILHADAS (PERSONAL E ALUNO)
                    auth.requestMatchers(
                            "/personais/*/horarios-disponiveis",
                            "/agendamentos/**",
                            "/usuarios/**"
                    ).hasAnyRole("PERSONAL", "ALUNO");

                    // 5. ROTAS GENÉRICAS DO PERSONAL (depois das específicas)
                    auth.requestMatchers(
                            "/personais/**",
                            "/produtos-exibicoes/**"
                    ).hasRole("PERSONAL");

                    // 6. ROTAS GENÉRICAS DO ALUNO (depois das específicas)
                    auth.requestMatchers(
                            "/alunos/**",
                            "/comprar/**",
                            "/checkouts/**",
                            "/produtos-contratados/**"
                    ).hasRole("ALUNO");

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
