package com.vitalwatch.center.platform.iam.infrastructure.persistence.jpa.assemblers;

import com.vitalwatch.center.platform.iam.domain.model.aggregates.UserAccount;
import com.vitalwatch.center.platform.iam.domain.model.valueobjects.EmailAddress;
import com.vitalwatch.center.platform.iam.infrastructure.persistence.jpa.entities.UserAccountPersistenceEntity;

/**
 * Assembler between UserAccount domain and persistence representations.
 */
public final class UserAccountPersistenceAssembler {

    private UserAccountPersistenceAssembler() {
    }

    public static UserAccount toDomainFromPersistence(UserAccountPersistenceEntity entity) {
        return new UserAccount(
                entity.getId(),
                entity.getHospitalWorkspaceId(),
                entity.getProfileId(),
                new EmailAddress(entity.getEmailAddress()),
                entity.getRole(),
                entity.getStatus()
        );
    }

    public static UserAccountPersistenceEntity toPersistenceFromDomain(UserAccount aggregate) {
        var entity = new UserAccountPersistenceEntity();
        entity.setId(aggregate.getId());
        entity.setHospitalWorkspaceId(aggregate.getHospitalWorkspaceId());
        entity.setProfileId(aggregate.getProfileId());
        entity.setEmailAddress(aggregate.getEmailAddress());
        entity.setRole(aggregate.getRole());
        entity.setStatus(aggregate.getStatus());
        return entity;
    }
}