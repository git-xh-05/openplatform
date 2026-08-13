package com.openplatform.open.config;

import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;

@Configuration
public class OpenPlatformConfiguration {

    @Bean
    public GroupedOpenApi openPlatformApi() {
        return GroupedOpenApi.builder().group("open-platform").displayName("开放平台").pathsToMatch("/open-platform/**", "/open-api/**").build();
    }

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        return builder
            .connectTimeout(Duration.ofSeconds(5))
            .readTimeout(Duration.ofSeconds(30))
            .build();
    }
}