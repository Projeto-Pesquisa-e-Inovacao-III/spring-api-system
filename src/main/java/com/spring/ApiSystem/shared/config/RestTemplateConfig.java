package com.spring.ApiSystem.shared.config;

import com.infobip.ApiClient;
import com.infobip.api.WhatsAppApi;
import com.infobip.ApiKey;
import com.infobip.BaseUrl;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;


@Configuration
public class RestTemplateConfig {
    @Value("${infobip.base.url}")
    private String infobipBaseUrl;
    @Value("${infobip.api.key}")
    private String infobipApiKey;


    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }


    @Bean
    public WhatsAppApi whatsAppApi() {

        ApiClient apiClient = ApiClient.forApiKey(ApiKey.from(infobipApiKey))
                .withBaseUrl(BaseUrl.from(infobipBaseUrl))
                .build();
        return new WhatsAppApi(apiClient);
    }
}
