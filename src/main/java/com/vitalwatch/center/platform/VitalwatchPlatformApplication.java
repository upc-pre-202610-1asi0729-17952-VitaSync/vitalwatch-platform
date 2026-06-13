package com.vitalwatch.center.platform;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

/**
 * Bootstrap class for the VitalWatch Platform application.
 */
@EnableJpaAuditing
@SpringBootApplication
public class VitalwatchPlatformApplication {

    public static void main(String[] args) {
        SpringApplication.run(VitalwatchPlatformApplication.class, args);
    }

}
