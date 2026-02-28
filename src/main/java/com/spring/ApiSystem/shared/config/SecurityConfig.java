package com.spring.ApiSystem.shared.config;

import com.spring.ApiSystem.shared.config.filter.FilterService;
import com.spring.ApiSystem.shared.config.helper.SecurityAuthorizationHelper;
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

import org.springframework.web.servlet.handler.HandlerMappingIntrospector;


@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    private final FilterService filterService;
    private final CorsConfig corsConfig;
    private final SecurityAuthorizationHelper securityAuthorizationHelper;

    @Value("${spring.profiles.active:}")
    private String perfilAtivo;

    public SecurityConfig(FilterService filterService, CorsConfig corsConfig, SecurityAuthorizationHelper securityAuthorizationHelper) {
        this.filterService = filterService;
        this.corsConfig = corsConfig;
        this.securityAuthorizationHelper = securityAuthorizationHelper;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity httpSecurity,
            HandlerMappingIntrospector introspector
    ) throws Exception {

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

                    // 1) ROTAS PÚBLICAS
                    auth.requestMatchers(HttpMethod.OPTIONS, "/**").permitAll();

                    auth.requestMatchers(HttpMethod.GET,
                            "/api/produtos-exibicoes/ativos"
                    ).permitAll();

                    auth.requestMatchers(HttpMethod.POST,
                            "/api/alunos/cadastro",
                            "/api/personais/cadastro",
                            "/api/usuarios/login",
                            "/api/produtos-contratados/pagamento",
                            "/api/password-reset/**"
                    ).permitAll();

                    auth.requestMatchers(HttpMethod.GET,
                            "/api/produtos-exibicoes",
                            "/api/usuarios/auth"
                    ).permitAll();

                    // 2) ESPECÍFICAS DO ALUNO
                    auth.requestMatchers(HttpMethod.GET,
                            "/api/produtos-contratados/total-tipo/*",
                            "/api/personais"
                    ).access(securityAuthorizationHelper.roleIfExists(introspector, "ROLE_ALUNO"));

                    // 3) ESPECÍFICAS DO PERSONAL
                    auth.requestMatchers(HttpMethod.GET,
                            "/api/alunos",
                            "/api/alunos/*"
                    ).access(securityAuthorizationHelper.roleIfExists(introspector, "ROLE_PERSONAL"));

                    auth.requestMatchers(
                            "/api/agendamentos/*/confirmar-conclusao",
                            "/api/agendamentos/ausencia",
                            "/api/agendamentos/consultoria-realizadas/*",
                            "/api/agendamentos/contagem-status-data",
                            "/api/produtos-contratados/ganhos-mes/*",
                            "/api/produtos-contratados/planos-vendidos/*",
                            "/api/produtos-contratados/quantidade-e-percentual-alunos-expirados"
                    ).access(securityAuthorizationHelper.roleIfExists(introspector, "ROLE_PERSONAL"));

                    // 4) COMPARTILHADAS
                    auth.requestMatchers(
                            "/api/personais/*/horarios-disponiveis",
                            "/api/agendamentos/**",
                            "/api/usuarios/**"
                    ).access(securityAuthorizationHelper.anyRoleIfExists(introspector, "ROLE_PERSONAL", "ROLE_ALUNO"));

                    // 5) GENÉRICAS DO PERSONAL
                    auth.requestMatchers(
                            "/api/personais/**",
                            "/api/produtos-exibicoes/**"
                    ).access(securityAuthorizationHelper.roleIfExists(introspector, "ROLE_PERSONAL"));

                    // 6) GENÉRICAS DO ALUNO
                    auth.requestMatchers(
                            "/api/alunos/**",
                            "/api/comprar/**",
                            "/api/checkouts/**",
                            "/api/produtos-contratados/**"
                    ).access(securityAuthorizationHelper.roleIfExists(introspector, "ROLE_ALUNO"));

                    auth.anyRequest().access(securityAuthorizationHelper.authenticatedIfExists(introspector));
                })
                .addFilterBefore(new CorsFilter(corsConfig.corsConfigurationSource()),
                        UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(filterService, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

}