package com.vitalwatch.center.platform.shared.infrastructure.security.configuration;

import com.vitalwatch.center.platform.iam.infrastructure.security.filters.JwtAuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Security configuration for the platform API.
 */
@Configuration
public class SecurityConfiguration {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    public SecurityConfiguration(JwtAuthenticationFilter jwtAuthenticationFilter) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(Customizer.withDefaults())
                .httpBasic(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .logout(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers(
                                "/",
                                "/error",
                                "/swagger-ui.html",
                                "/swagger-ui/**",
                                "/api-docs",
                                "/api-docs/**",
                                "/v3/api-docs",
                                "/v3/api-docs/**"
                        ).permitAll()

                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()

                        .requestMatchers(HttpMethod.POST, "/authentication/sign-in").permitAll()
                        .requestMatchers(HttpMethod.POST, "/authentication/sign-up").permitAll()
                        .requestMatchers(HttpMethod.GET, "/authentication/me").authenticated()

                        .requestMatchers(HttpMethod.GET, "/plans/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/billing/create-checkout-session").permitAll()
                        .requestMatchers(HttpMethod.GET, "/billing/checkout-session-status").permitAll()
                        .requestMatchers(HttpMethod.POST, "/billing/activate-checkout-session").permitAll()

                        /*
                         * Temporarily public while the registration, checkout,
                         * and invitation flows are completed.
                         */
                        .requestMatchers(HttpMethod.POST, "/organizations").permitAll()
                        .requestMatchers(HttpMethod.POST, "/users").permitAll()

                        .requestMatchers(HttpMethod.GET, "/organizations/**")
                        .hasRole("HOSPITAL_ADMIN")

                        .requestMatchers(HttpMethod.GET, "/users/**")
                        .hasAnyRole("HOSPITAL_ADMIN", "SUPERVISOR")

                        .requestMatchers(HttpMethod.GET, "/subscriptions/**")
                        .hasRole("HOSPITAL_ADMIN")

                        .requestMatchers(HttpMethod.POST, "/subscriptions")
                        .hasRole("HOSPITAL_ADMIN")

                        .anyRequest().authenticated()
                )
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}