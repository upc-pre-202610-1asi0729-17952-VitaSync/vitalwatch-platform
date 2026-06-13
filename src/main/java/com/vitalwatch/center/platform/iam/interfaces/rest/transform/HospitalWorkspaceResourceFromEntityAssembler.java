package com.vitalwatch.center.platform.iam.interfaces.rest.transform;

import com.vitalwatch.center.platform.iam.domain.model.aggregates.HospitalWorkspace;
import com.vitalwatch.center.platform.iam.interfaces.rest.resources.HospitalWorkspaceResource;

/**
 * Assembler to convert HospitalWorkspace aggregate into HospitalWorkspaceResource.
 */
public final class HospitalWorkspaceResourceFromEntityAssembler {

    private HospitalWorkspaceResourceFromEntityAssembler() {
    }

    public static HospitalWorkspaceResource toResourceFromEntity(HospitalWorkspace entity) {
        return new HospitalWorkspaceResource(
                entity.getId(),
                entity.getName(),
                entity.getRuc(),
                entity.getAdministratorProfileId(),
                entity.getAdministratorEmail(),
                entity.getStatus()
        );
    }
}