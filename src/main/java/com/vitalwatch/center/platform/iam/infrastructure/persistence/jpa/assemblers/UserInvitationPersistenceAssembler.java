package com.vitalwatch.center.platform.iam.infrastructure.persistence.jpa.assemblers;

import com.vitalwatch.center.platform.iam.domain.model.aggregates.UserInvitation;
import com.vitalwatch.center.platform.iam.domain.model.valueobjects.EmailAddress;
import com.vitalwatch.center.platform.iam.infrastructure.persistence.jpa.entities.UserInvitationPersistenceEntity;

/**
 * Assembler between UserInvitation domain and persistence representations.
 */
public final class UserInvitationPersistenceAssembler {

    private UserInvitationPersistenceAssembler() {
    }

    public static UserInvitation toDomainFromPersistence(UserInvitationPersistenceEntity entity) {
        return new UserInvitation(
                entity.getId(),
                entity.getHospitalWorkspaceId(),
                new EmailAddress(entity.getEmailAddress()),
                entity.getRole(),
                entity.getStatus(),
                entity.getToken(),
                entity.getInvitedAt(),
                entity.getExpiresAt(),
                entity.getAcceptedAt()
        );
    }

    public static UserInvitationPersistenceEntity toPersistenceFromDomain(UserInvitation aggregate) {
        var entity = new UserInvitationPersistenceEntity();
        entity.setId(aggregate.getId());
        entity.setHospitalWorkspaceId(aggregate.getHospitalWorkspaceId());
        entity.setEmailAddress(aggregate.getEmailAddress());
        entity.setRole(aggregate.getRole());
        entity.setStatus(aggregate.getStatus());
        entity.setToken(aggregate.getToken());
        entity.setInvitedAt(aggregate.getInvitedAt());
        entity.setExpiresAt(aggregate.getExpiresAt());
        entity.setAcceptedAt(aggregate.getAcceptedAt());
        return entity;
    }
}