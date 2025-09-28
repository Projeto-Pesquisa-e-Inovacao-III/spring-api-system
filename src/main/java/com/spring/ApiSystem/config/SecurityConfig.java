package com.spring.ApiSystem.config;

import com.spring.ApiSystem.service.FilterService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {
    private final FilterService filterService;

    public SecurityConfig(FilterService filterService) {
        this.filterService = filterService;
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
                        .anyRequest().authenticated()
                )
                .addFilterBefore(filterService, UsernamePasswordAuthenticationFilter.class)
                .build();
    }
}
