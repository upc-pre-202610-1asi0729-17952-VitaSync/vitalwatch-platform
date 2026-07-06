package com.vitalwatch.center.platform.iam.application.internal.outboundservices;

import com.vitalwatch.center.platform.iam.infrastructure.persistence.jpa.entities.UserJpaEntity;

/**
 * Service contract for JWT token generation.
 */
public interface TokenService {

    String generateToken(UserJpaEntity user);
}