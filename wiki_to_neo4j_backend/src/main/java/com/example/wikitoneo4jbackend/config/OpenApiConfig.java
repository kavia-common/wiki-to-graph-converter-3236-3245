package com.example.wikitoneo4jbackend.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * PUBLIC_INTERFACE
 * Configures OpenAPI metadata for the application.
 */
@Configuration
public class OpenApiConfig {

    // PUBLIC_INTERFACE
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Wiki to Graph Converter API")
                        .description("Converts wiki content to a graph schema with nodes and relationships")
                        .version("0.1.0"));
    }
}
