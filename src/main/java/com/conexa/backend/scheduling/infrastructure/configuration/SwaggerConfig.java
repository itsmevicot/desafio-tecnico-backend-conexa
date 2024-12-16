package com.conexa.backend.scheduling.infrastructure.configuration;

import io.github.cdimascio.dotenv.Dotenv;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    private final Dotenv dotenv = Dotenv.load();

    @Bean
    public OpenAPI customOpenAPI() {
        String serverUrl = dotenv.get("SWAGGER_SERVER_URL");

        return new OpenAPI()
                .info(new Info()
                        .title("Conexa Scheduling API")
                        .version("1.0.0")
                        .description("API Documentation for Conexa Scheduling"))
                .addServersItem(new Server()
                        .url(serverUrl)
                        .description("Base URL for API v1"));
    }
}
