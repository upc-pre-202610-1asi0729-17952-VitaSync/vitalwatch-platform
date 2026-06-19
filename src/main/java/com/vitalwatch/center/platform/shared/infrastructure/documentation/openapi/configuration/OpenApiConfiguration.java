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
 * Configures OpenAPI documentation.
 */
@Configuration
public class OpenApiConfiguration {

    @Value("${spring.application.name}")
    String applicationName;

    @Value("${documentation.application.description}")
    String applicationDescription;

    @Value("${documentation.application.version}")
    String applicationVersion;

    @Value("${documentation.server.url}")
    String serverUrl;

    @Bean
    public OpenAPI vitalWatchOpenApi() {
        var openApi = new OpenAPI();

        openApi.info(new Info()
                        .title(this.applicationName)
                        .description(this.applicationDescription)
                        .version(this.applicationVersion)
                        .contact(new Contact()
                                .name("VitalWatch Support")
                                .email("support@vitalwatch.com")
                                .url("https://vitalwatch.com/support"))
                        .license(new License()
                                .name("Apache 2.0")
                                .url("https://www.apache.org/licenses/LICENSE-2.0.html")))
                .externalDocs(new ExternalDocumentation()
                        .description("VitalWatch Platform Documentation")
                        .url("https://vitalwatch.com/docs"));

        openApi.servers(List.of(
                new Server()
                        .url(this.serverUrl)
                        .description("Application Server")
        ));

        return openApi;
    }
}
