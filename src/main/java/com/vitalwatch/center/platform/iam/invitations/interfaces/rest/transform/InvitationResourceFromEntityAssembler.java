package com.vitalwatch.center.platform.iam.invitations.interfaces.rest.transform;

import com.vitalwatch.center.platform.iam.invitations.infrastructure.persistence.jpa.entities.InvitationJpaEntity;
import com.vitalwatch.center.platform.iam.invitations.interfaces.rest.resources.InvitationResource;

/**
 * Assembler used to convert invitation entities into REST resources.
 */
public final class InvitationResourceFromEntityAssembler {

    private InvitationResourceFromEntityAssembler() {
    }

    public static InvitationResource toResourceFromEntity(InvitationJpaEntity entity) {
        return new InvitationResource(
                entity.getId(),
                entity.getToken(),
                entity.getEmail(),
                entity.getRole().name(),
                entity.getStatus().name(),
                entity.getOrganization().getId(),
                entity.getOrganization().getCommercialName(),
                entity.getSpecialtyId(),
                entity.getWorkAreaId(),
                entity.getAcceptedUserId(),
                entity.getExpiresAt(),
                entity.getCreatedAt()
        );
    }
}