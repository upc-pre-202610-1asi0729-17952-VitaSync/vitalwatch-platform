package com.vitalwatch.center.platform.shared.infrastructure.cors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Global CORS configuration for local development and production deployment.
 */
@Configuration
public class GlobalCorsConfiguration implements WebMvcConfigurer {

    @Value("${application.cors.allowed-origins:http://localhost:4200,http://127.0.0.1:4200,http://localhost:3000,http://127.0.0.1:3000}")
    private String allowedOrigins;

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        var origins = allowedOrigins.split(",");

        registry.addMapping("/api/**")
                .allowedOrigins(trimOrigins(origins))
                .allowedMethods("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS")
                .allowedHeaders("*")
                .exposedHeaders("Location")
                .allowCredentials(false)
                .maxAge(3600);
    }

    private String[] trimOrigins(String[] origins) {
        for (var index = 0; index < origins.length; index++) {
            origins[index] = origins[index].trim();
        }

        return origins;
    }
}