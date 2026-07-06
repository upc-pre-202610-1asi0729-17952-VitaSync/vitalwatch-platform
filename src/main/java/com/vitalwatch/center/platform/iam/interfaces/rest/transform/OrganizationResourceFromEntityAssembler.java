package com.vitalwatch.center.platform.iam.interfaces.rest.transform;

import com.vitalwatch.center.platform.iam.infrastructure.persistence.jpa.entities.OrganizationJpaEntity;
import com.vitalwatch.center.platform.iam.interfaces.rest.resources.OrganizationResource;

/**
 * Assembler used to convert organization entities into REST resources.
 */
public final class OrganizationResourceFromEntityAssembler {

    private OrganizationResourceFromEntityAssembler() {
    }

    public static OrganizationResource toResourceFromEntity(OrganizationJpaEntity entity) {
        return new OrganizationResource(
                entity.getId(),
                entity.getLegalName(),
                entity.getCommercialName(),
                entity.getRuc(),
                entity.getEmail(),
                entity.getPhone(),
                entity.getAddress(),
                entity.getStatus().name()
        );
    }
}