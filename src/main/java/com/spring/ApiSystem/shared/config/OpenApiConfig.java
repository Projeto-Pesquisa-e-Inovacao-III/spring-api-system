package com.spring.ApiSystem.shared.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {
    @Bean
    public OpenAPI apiInfo(){
        return new OpenAPI()
                .info(new Info()
                        .title("spring-api-system")
                        .description("Documentação de todos os endpoints disponíveis")
                        .version("1.0.0"));
    }

    @Bean
    public GroupedOpenApi usuariosApi() {
        return GroupedOpenApi.builder()
                .group("Usuários")
                .pathsToMatch("/api/usuarios/**")
                .build();
    }

    @Bean
    public GroupedOpenApi enderecosApi() {
        return GroupedOpenApi.builder()
                .group("Endereços")
                .pathsToMatch("/api/enderecos/**")
                .build();
    }

    @Bean
    public GroupedOpenApi agendamentoApi() {
        return GroupedOpenApi.builder()
                .group("Agendamento")
                .pathsToMatch("/api/agendamentos/**")
                .build();
    }

    @Bean
    public GroupedOpenApi alunoApi() {
        return GroupedOpenApi.builder()
                .group("Aluno")
                .pathsToMatch("/api/alunos/**")
                .build();
    }

    @Bean
    public GroupedOpenApi personalApi() {
        return GroupedOpenApi.builder()
                .group("Personal")
                .pathsToMatch("/api/personais/**")
                .build();
    }

    @Bean
    public GroupedOpenApi produtoContratadoApi() {
        return GroupedOpenApi.builder()
                .group("Produto Contratado")
                .pathsToMatch("/api/produtos-contratados/**")
                .build();
    }

    @Bean
    public GroupedOpenApi produtoExibicaoApi() {
        return GroupedOpenApi.builder()
                .group("Produto Exibição")
                .pathsToMatch("/api/produtos-exibicoes/**")
                .build();
    }

    @Bean
    public GroupedOpenApi allApi() {
        return GroupedOpenApi.builder()
                .group("API Completa")
                .pathsToMatch("/api/**")
                .build();
    }
}
