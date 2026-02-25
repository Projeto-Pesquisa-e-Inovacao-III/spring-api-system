package com.spring.ApiSystem.shared.config;

import com.spring.ApiSystem.shared.config.filter.FilterService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authorization.AuthenticatedAuthorizationManager;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.intercept.RequestAuthorizationContext;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.servlet.HandlerExecutionChain;
import org.springframework.web.servlet.HandlerMapping;
import org.springframework.web.servlet.handler.HandlerMappingIntrospector;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    private final FilterService filterService;
    private final CorsConfig corsConfig;

    public SecurityConfig(FilterService filterService, CorsConfig corsConfig) {
        this.filterService = filterService;
        this.corsConfig = corsConfig;
    }

    @Value("${spring.profiles.active}")
    private String perfilAtivo;

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
                    ).access(roleIfExists(introspector, "ROLE_ALUNO"));

                    // 3) ESPECÍFICAS DO PERSONAL
                    auth.requestMatchers(HttpMethod.GET,
                            "/api/alunos",
                            "/api/alunos/*"
                    ).access(roleIfExists(introspector, "ROLE_PERSONAL"));

                    auth.requestMatchers(
                            "/api/agendamentos/*/confirmar-conclusao",
                            "/api/agendamentos/ausencia",
                            "/api/agendamentos/consultoria-realizadas/*",
                            "/api/agendamentos/contagem-status-data",
                            "/api/produtos-contratados/ganhos-mes/*",
                            "/api/produtos-contratados/planos-vendidos/*",
                            "/api/produtos-contratados/quantidade-e-percentual-alunos-expirados"
                    ).access(roleIfExists(introspector, "ROLE_PERSONAL"));

                    // 4) COMPARTILHADAS
                    auth.requestMatchers(
                            "/api/personais/*/horarios-disponiveis",
                            "/api/agendamentos/**",
                            "/api/usuarios/**"
                    ).access(anyRoleIfExists(introspector, "ROLE_PERSONAL", "ROLE_ALUNO"));

                    // 5) GENÉRICAS DO PERSONAL
                    auth.requestMatchers(
                            "/api/personais/**",
                            "/api/produtos-exibicoes/**"
                    ).access(roleIfExists(introspector, "ROLE_PERSONAL"));

                    // 6) GENÉRICAS DO ALUNO
                    auth.requestMatchers(
                            "/api/alunos/**",
                            "/api/comprar/**",
                            "/api/checkouts/**",
                            "/api/produtos-contratados/**"
                    ).access(roleIfExists(introspector, "ROLE_ALUNO"));

                    // fallback: existe endpoint -> autenticado; não existe -> 404
                    auth.anyRequest().access(authenticatedIfExists(introspector));
                })
                .addFilterBefore(new CorsFilter(corsConfig.corsConfigurationSource()),
                        UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(filterService, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    // =========================
    // Helpers para 404 vs 403/401
    // =========================
    private boolean handlerExists(HttpServletRequest request, HandlerMappingIntrospector introspector) {
        try {
            HandlerMapping mapping = introspector.getMatchableHandlerMapping(request);
            if (mapping == null) return false;

            HandlerExecutionChain chain = mapping.getHandler(request);
            return chain != null;
        } catch (Exception ex) {
            return true;
        }
    }

    private AuthorizationManager<RequestAuthorizationContext> authenticatedIfExists(
            HandlerMappingIntrospector introspector
    ) {
        return (authentication, context) -> {
            HttpServletRequest request = context.getRequest();

            if (!handlerExists(request, introspector)) {
                return new AuthorizationDecision(true);
            }

            return AuthenticatedAuthorizationManager.authenticated()
                    .check(authentication, context);
        };
    }

    private AuthorizationManager<RequestAuthorizationContext> roleIfExists(
            HandlerMappingIntrospector introspector,
            String roleAuthority
    ) {
        return (authentication, context) -> {
            HttpServletRequest request = context.getRequest();

            if (!handlerExists(request, introspector)) {
                return new AuthorizationDecision(true);
            }

            var authDecision = AuthenticatedAuthorizationManager.authenticated()
                    .check(authentication, context);
            if (!authDecision.isGranted()) return authDecision;

            boolean hasRole = authentication.get().getAuthorities().stream()
                    .anyMatch(a -> a.getAuthority().equals(roleAuthority));

            return new AuthorizationDecision(hasRole);
        };
    }

    private AuthorizationManager<RequestAuthorizationContext> anyRoleIfExists(
            HandlerMappingIntrospector introspector,
            String... roleAuthorities
    ) {
        return (authentication, context) -> {
            HttpServletRequest request = context.getRequest();

            if (!handlerExists(request, introspector)) {
                return new AuthorizationDecision(true);
            }

            var authDecision = AuthenticatedAuthorizationManager.authenticated()
                    .check(authentication, context);
            if (!authDecision.isGranted()) return authDecision;

            var granted = authentication.get().getAuthorities().stream()
                    .map(a -> a.getAuthority())
                    .anyMatch(a -> Arrays.asList(roleAuthorities).contains(a));

            return new AuthorizationDecision(granted);
        };
    }



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