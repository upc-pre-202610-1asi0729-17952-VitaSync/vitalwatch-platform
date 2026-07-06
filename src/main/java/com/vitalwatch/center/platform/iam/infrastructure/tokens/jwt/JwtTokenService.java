package com.vitalwatch.center.platform.iam.infrastructure.tokens.jwt;

import com.vitalwatch.center.platform.iam.application.internal.outboundservices.TokenService;
import com.vitalwatch.center.platform.iam.infrastructure.persistence.jpa.entities.UserJpaEntity;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

/**
 * JWT token service implementation.
 */
@Service
public class JwtTokenService implements TokenService {

    private final String secret;
    private final Long expirationMinutes;

    public JwtTokenService(
            @Value("${application.security.jwt.secret}") String secret,
            @Value("${application.security.jwt.expiration-minutes}") Long expirationMinutes
    ) {
        this.secret = secret;
        this.expirationMinutes = expirationMinutes;
    }

    @Override
    public String generateToken(UserJpaEntity user) {
        var issuedAt = Instant.now();
        var expiration = issuedAt.plus(expirationMinutes, ChronoUnit.MINUTES);

        return Jwts.builder()
                .subject(user.getEmail())
                .claim("userId", user.getId())
                .claim("username", user.getUsername())
                .claim("role", user.getRole().name())
                .claim("organizationId", getOrganizationId(user))
                .issuedAt(Date.from(issuedAt))
                .expiration(Date.from(expiration))
                .signWith(getSigningKey())
                .compact();
    }

    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }

    private Long getOrganizationId(UserJpaEntity user) {
        return user.getOrganization() == null
                ? null
                : user.getOrganization().getId();
    }
}