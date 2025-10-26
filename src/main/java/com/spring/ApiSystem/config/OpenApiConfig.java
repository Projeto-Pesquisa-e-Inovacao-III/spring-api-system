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
                .group("usuários")
                .pathsToMatch("/usuarios/**")
                .build();
    }

    @Bean
    public GroupedOpenApi enderecosApi() {
        return GroupedOpenApi.builder()
                .group("endereços")
                .pathsToMatch("/enderecos/**")
                .build();
    }

    @Bean
    public GroupedOpenApi produtoExibicaoApi() {
        return GroupedOpenApi.builder()
                .group("produtos de exibição")
                .pathsToMatch("/produtosExibicao/**")
                .build();
    }
}