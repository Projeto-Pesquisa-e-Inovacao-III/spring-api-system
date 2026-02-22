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
                .pathsToMatch("/usuarios/**")
                .build();
    }

    @Bean
    public GroupedOpenApi enderecosApi() {
        return GroupedOpenApi.builder()
                .group("Endereços")
                .pathsToMatch("/enderecos/**")
                .build();
    }

    @Bean
    public GroupedOpenApi agendamentoApi() {
        return GroupedOpenApi.builder()
                .group("Agendamento")
                .pathsToMatch("/agendamentos/**")
                .build();
    }

    @Bean
    public GroupedOpenApi alunoAPi() {
        return GroupedOpenApi.builder()
                .group("Aluno")
                .pathsToMatch("/alunos/**")
                .build();
    }

    @Bean
    public GroupedOpenApi personalAPi() {
        return GroupedOpenApi.builder()
                .group("Personal")
                .pathsToMatch("/personais/**")
                .build();
    }

    @Bean
    public GroupedOpenApi produtoContratadoAPi() {
        return GroupedOpenApi.builder()
                .group("Produto Contratado")
                .pathsToMatch("/produtos-contratados/**")
                .build();
    }

    @Bean
    public GroupedOpenApi produtoExibicaoAPi() {
        return GroupedOpenApi.builder()
                .group("Produto Exibição")
                .pathsToMatch("/produtos-exibicoes/**")
                .build();
    }


}