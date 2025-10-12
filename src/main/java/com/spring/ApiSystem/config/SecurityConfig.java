package com.spring.ApiSystem.config;

import com.spring.ApiSystem.service.FilterService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
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
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers(HttpMethod.POST, "/usuarios/cadastro").permitAll()
                        .requestMatchers(HttpMethod.POST, "/usuarios/login").permitAll()
                        .requestMatchers(HttpMethod.GET,
                                "/v3/api-docs/**",
                                "/swagger-ui/**",
                                "/doc"
                        ).permitAll()
                        .anyRequest().authenticated()
                )
                .addFilterBefore(new CorsFilter(corsConfig.corsConfigurationSource()),
                                 UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(filterService, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    public Argon2PasswordEncoder argon2PasswordEncoder() {
        return new Argon2PasswordEncoder(16,
                                        32,
                                        1,
                                        65536,
                                        3);
    }
}
