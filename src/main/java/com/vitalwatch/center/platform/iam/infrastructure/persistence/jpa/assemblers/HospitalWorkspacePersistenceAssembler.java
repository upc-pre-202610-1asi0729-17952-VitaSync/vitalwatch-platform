package com.vitalwatch.center.platform.iam.infrastructure.persistence.jpa.assemblers;

import com.vitalwatch.center.platform.iam.domain.model.aggregates.HospitalWorkspace;
import com.vitalwatch.center.platform.iam.domain.model.valueobjects.EmailAddress;
import com.vitalwatch.center.platform.iam.domain.model.valueobjects.HospitalWorkspaceName;
import com.vitalwatch.center.platform.iam.domain.model.valueobjects.Ruc;
import com.vitalwatch.center.platform.iam.infrastructure.persistence.jpa.entities.HospitalWorkspacePersistenceEntity;

/**
 * Assembler between HospitalWorkspace domain and persistence representations.
 */
public final class HospitalWorkspacePersistenceAssembler {

    private HospitalWorkspacePersistenceAssembler() {
    }

    public static HospitalWorkspace toDomainFromPersistence(HospitalWorkspacePersistenceEntity entity) {
        return new HospitalWorkspace(
                entity.getId(),
                new HospitalWorkspaceName(entity.getName()),
                new Ruc(entity.getRuc()),
                entity.getAdministratorProfileId(),
                new EmailAddress(entity.getAdministratorEmail()),
                entity.getStatus()
        );
    }

    public static HospitalWorkspacePersistenceEntity toPersistenceFromDomain(HospitalWorkspace aggregate) {
        var entity = new HospitalWorkspacePersistenceEntity();
        entity.setId(aggregate.getId());
        entity.setName(aggregate.getName());
        entity.setRuc(aggregate.getRuc());
        entity.setAdministratorProfileId(aggregate.getAdministratorProfileId());
        entity.setAdministratorEmail(aggregate.getAdministratorEmail());
        entity.setStatus(aggregate.getStatus());
        return entity;
    }
}