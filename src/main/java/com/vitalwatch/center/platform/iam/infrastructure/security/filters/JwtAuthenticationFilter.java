package com.vitalwatch.center.platform.iam.infrastructure.security.filters;

import com.vitalwatch.center.platform.iam.application.internal.outboundservices.TokenService;
import com.vitalwatch.center.platform.iam.infrastructure.persistence.jpa.repositories.UserJpaRepository;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

/**
 * Filter responsible for reading and validating JWT tokens from requests.
 */
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String BEARER_PREFIX = "Bearer ";

    private final TokenService tokenService;
    private final UserJpaRepository userRepository;

    public JwtAuthenticationFilter(
            TokenService tokenService,
            UserJpaRepository userRepository
    ) {
        this.tokenService = tokenService;
        this.userRepository = userRepository;
    }

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {
        var authorizationHeader = request.getHeader(AUTHORIZATION_HEADER);

        if (authorizationHeader == null || !authorizationHeader.startsWith(BEARER_PREFIX)) {
            filterChain.doFilter(request, response);
            return;
        }

        var token = authorizationHeader.substring(BEARER_PREFIX.length());

        try {
            var email = tokenService.extractSubject(token);

            if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                var user = userRepository.findByEmail(email);

                if (user.isPresent() && tokenService.isTokenValid(token, user.get())) {
                    var authenticatedUser = user.get();

                    var authorities = List.of(
                            new SimpleGrantedAuthority("ROLE_" + authenticatedUser.getRole().name())
                    );

                    var authenticationToken = new UsernamePasswordAuthenticationToken(
                            authenticatedUser,
                            null,
                            authorities
                    );

                    authenticationToken.setDetails(
                            new WebAuthenticationDetailsSource().buildDetails(request)
                    );

                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                }
            }
        } catch (JwtException | IllegalArgumentException ignored) {
            SecurityContextHolder.clearContext();
        }

        filterChain.doFilter(request, response);
    }
}