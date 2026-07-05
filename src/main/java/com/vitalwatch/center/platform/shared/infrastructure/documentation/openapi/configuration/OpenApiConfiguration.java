package com.vitalwatch.center.platform.shared.infrastructure.documentation.openapi.configuration;

import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * OpenAPI configuration for VitalWatch Platform API documentation.
 */
@Configuration
public class OpenApiConfiguration {

    @Value("${documentation.server.url:http://localhost:8080/api/v1}")
    private String serverUrl;

    @Bean
    public OpenAPI vitalWatchOpenApi() {
        return new OpenAPI()
                .servers(List.of(new Server()
                        .url(serverUrl)
                        .description("VitalWatch Platform API Server")))
                .info(new Info()
                        .title("VitalWatch Platform API")
                        .description("REST API for VitalWatch hospital fatigue monitoring platform.")
                        .version("v1")
                        .contact(new Contact()
                                .name("VitaSync")
                                .email("support@vitalwatch.local"))
                        .license(new License()
                                .name("Apache 2.0")
                                .url("https://www.apache.org/licenses/LICENSE-2.0.html")))
                .externalDocs(new ExternalDocumentation()
                        .description("VitalWatch Repository")
                        .url("https://github.com/upc-pre-202610-1asi0729-17952-VitaSync/vitalwatch-platform"));
    }
}