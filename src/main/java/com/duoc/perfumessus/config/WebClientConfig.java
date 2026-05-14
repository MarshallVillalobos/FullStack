package com.duoc.perfumessus.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    @Value("${fragella.base-url}")
    private String fragellaBaseUrl;

    @Value("${fragella.api-key}")
    private String apiKey;

    @Bean
    public WebClient fragellaWebClient() {
        return WebClient.builder()
                .baseUrl(fragellaBaseUrl)
                .defaultHeader("Accept", "application/json")
                //Inyectamos la llave de autorización globalmente
                .defaultHeader("x-api-key", apiKey) 
                .build();
    }
}