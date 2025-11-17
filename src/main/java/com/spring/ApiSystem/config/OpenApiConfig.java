package com.spring.ApiSystem.config;

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
                .group("usuário")
                .pathsToMatch("/usuarios/**")
                .build();
    }

    @Bean
    public GroupedOpenApi enderecosApi() {
        return GroupedOpenApi.builder()
                .group("endereço")
                .pathsToMatch("/enderecos/**")
                .build();
    }

    @Bean
    public GroupedOpenApi agendamentoApi() {
        return GroupedOpenApi.builder()
                .group("agendamento")
                .pathsToMatch("/agendamentos/**")
                .build();
    }

    @Bean
    public GroupedOpenApi alunoAPi() {
        return GroupedOpenApi.builder()
                .group("aluno")
                .pathsToMatch("/alunos/**")
                .build();
    }

    @Bean
    public GroupedOpenApi personalAPi() {
        return GroupedOpenApi.builder()
                .group("personal")
                .pathsToMatch("/personais/**")
                .build();
    }

    @Bean
    public GroupedOpenApi produtoContratadoAPi() {
        return GroupedOpenApi.builder()
                .group("produto contratado")
                .pathsToMatch("/produtos-contratados/**")
                .build();
    }

    @Bean
    public GroupedOpenApi produtoExibicaoAPi() {
        return GroupedOpenApi.builder()
                .group("produto exibição")
                .pathsToMatch("/produtos-exibicoes/**")
                .build();
    }
}