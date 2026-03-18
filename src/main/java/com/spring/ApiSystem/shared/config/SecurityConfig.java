package com.spring.ApiSystem.shared.config;

import com.spring.ApiSystem.shared.config.filter.FilterService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.filter.CorsFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    private final FilterService filterService;
    private final CorsConfig corsConfig;

    @Value("${spring.profiles.active:}")
    private String perfilAtivo;

    public SecurityConfig(FilterService filterService, CorsConfig corsConfig) {
        this.filterService = filterService;
        this.corsConfig = corsConfig;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {

        return httpSecurity
                .securityMatcher("/api/**")
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .headers(headers -> headers.frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin))
                .authorizeHttpRequests(auth -> {

                    if ("dev".equals(perfilAtivo) || "docker".equals(perfilAtivo)) {
                        auth.requestMatchers(HttpMethod.GET,
                                "/v3/api-docs/**",
                                "/swagger-ui/**",
                                "/doc"
                        ).permitAll();

                        auth.requestMatchers("/h2-console/**").permitAll();
                    }

                    // Públicas
                    auth.requestMatchers(HttpMethod.OPTIONS, "/**").permitAll();

                    auth.requestMatchers(HttpMethod.GET,
                            "/api/produtos-exibicoes/ativos",
                            "/api/produtos-exibicoes",
                            "/api/usuarios/auth"
                    ).permitAll();

                    auth.requestMatchers(HttpMethod.POST,
                            "/api/alunos/cadastro",
                            "/api/personais/cadastro",
                            "/api/usuarios/login",
                            "/api/produtos-contratados/pagamento",
                            "/api/password-reset/**"
                    ).permitAll();

                    // Compartilhadas (precisam vir antes de regras genéricas)
                    auth.requestMatchers(
                            "/api/personais/*/horarios-disponiveis",
                            "/api/agendamentos/**",
                            "/api/usuarios/**"
                    ).hasAnyAuthority("ROLE_PERSONAL", "ROLE_ALUNO");

                    // Aluno (mover para antes das regras genéricas para evitar shadowing)
                    auth.requestMatchers(HttpMethod.GET,
                            "/api/produtos-contratados/total-tipo/*",
                            "/api/personais"
                    ).hasAuthority("ROLE_ALUNO");

                    auth.requestMatchers(
                            "/api/alunos/**",
                            "/api/comprar/**",
                            "/api/checkouts/**",
                            "/api/produtos-contratados/**"
                    ).hasAuthority("ROLE_ALUNO");

                    // Personal (regras específicas para ROLE_PERSONAL)
                    auth.requestMatchers(HttpMethod.GET,
                            "/api/alunos",
                            "/api/alunos/*"
                    ).hasAuthority("ROLE_PERSONAL");

                    auth.requestMatchers(
                            "/api/agendamentos/*/confirmar-conclusao",
                            "/api/agendamentos/ausencia",
                            "/api/agendamentos/consultoria-realizadas/*",
                            "/api/agendamentos/contagem-status-data",
                            "/api/produtos-contratados/ganhos-mes/*",
                            "/api/produtos-contratados/planos-vendidos/*",
                            "/api/produtos-contratados/quantidade-e-percentual-alunos-expirados",
                            "/api/personais/**",
                            "/api/produtos-exibicoes/**"
                    ).hasAuthority("ROLE_PERSONAL");

                })
                .exceptionHandling(ex -> ex
                        .accessDeniedHandler((request, response, accessDeniedException) -> {
                            response.setStatus(403);
                            response.setContentType("application/json");
                            response.getWriter().write("""
                                {"status":403,"error":"Forbidden","message":"Acesso negado"}
                            """);
                        })
                )
                .addFilterBefore(new CorsFilter(corsConfig.corsConfigurationSource()),
                        UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(filterService, UsernamePasswordAuthenticationFilter.class)
                .build();
    }
}
