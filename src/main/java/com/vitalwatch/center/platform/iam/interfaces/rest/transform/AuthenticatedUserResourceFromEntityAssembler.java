package com.vitalwatch.center.platform.iam.interfaces.rest.transform;

import com.vitalwatch.center.platform.iam.infrastructure.persistence.jpa.entities.UserJpaEntity;
import com.vitalwatch.center.platform.iam.interfaces.rest.resources.AuthenticatedUserResource;

/**
 * Assembler used to convert authenticated users into REST resources.
 */
public final class AuthenticatedUserResourceFromEntityAssembler {

    private AuthenticatedUserResourceFromEntityAssembler() {
    }

    public static AuthenticatedUserResource toResourceFromEntity(
            UserJpaEntity entity,
            String token
    ) {
        var organizationId = entity.getOrganization() == null
                ? null
                : entity.getOrganization().getId();

        return new AuthenticatedUserResource(
                entity.getId(),
                entity.getFirstName(),
                entity.getLastName(),
                entity.getUsername(),
                entity.getEmail(),
                entity.getRole().name(),
                entity.getStatus().name(),
                organizationId,
                token
        );
    }
}