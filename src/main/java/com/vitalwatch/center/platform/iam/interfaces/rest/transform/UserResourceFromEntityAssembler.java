package com.vitalwatch.center.platform.iam.interfaces.rest.transform;

import com.vitalwatch.center.platform.iam.infrastructure.persistence.jpa.entities.UserJpaEntity;
import com.vitalwatch.center.platform.iam.interfaces.rest.resources.UserResource;

/**
 * Assembler used to convert user entities into REST resources.
 */
public final class UserResourceFromEntityAssembler {

    private UserResourceFromEntityAssembler() {
    }

    public static UserResource toResourceFromEntity(UserJpaEntity entity) {
        var organizationId = entity.getOrganization() == null
                ? null
                : entity.getOrganization().getId();

        return new UserResource(
                entity.getId(),
                entity.getFirstName(),
                entity.getLastName(),
                entity.getUsername(),
                entity.getEmail(),
                entity.getRole().name(),
                entity.getStatus().name(),
                organizationId,
                entity.getSpecialtyId(),
                entity.getWorkAreaId()
        );
    }
}