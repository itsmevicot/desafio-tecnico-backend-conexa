package com.conexa.backend.scheduling.infrastructure.configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Conexa Scheduling API")
                        .version("1.0.0")
                        .description("API Documentation for Conexa Scheduling"))
                .addServersItem(new Server()
                        .url("http://localhost:8080/api/v1")
                        .description("Base URL for API v1"));
    }
}
