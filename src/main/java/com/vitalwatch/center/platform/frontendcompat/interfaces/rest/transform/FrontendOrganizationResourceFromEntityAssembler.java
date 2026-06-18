package com.vitalwatch.center.platform.frontendcompat.interfaces.rest.transform;

import com.vitalwatch.center.platform.frontendcompat.interfaces.rest.resources.FrontendOrganizationResource;
import com.vitalwatch.center.platform.iam.domain.model.aggregates.HospitalWorkspace;
import com.vitalwatch.center.platform.iam.domain.model.enums.HospitalWorkspaceStatus;

/**
 * Assembler to expose HospitalWorkspace using the contract expected by the Angular frontend.
 */
public final class FrontendOrganizationResourceFromEntityAssembler {

    private FrontendOrganizationResourceFromEntityAssembler() {
    }

    public static FrontendOrganizationResource toResourceFromEntity(HospitalWorkspace entity) {
        return toResourceFromEntity(entity, "", "", 0L);
    }

    public static FrontendOrganizationResource toResourceFromEntity(
            HospitalWorkspace entity,
            String address,
            String phone,
            Long planId
    ) {
        return new FrontendOrganizationResource(
                entity.getId(),
                entity.getName(),
                entity.getRuc(),
                address,
                phone,
                toFrontendStatus(entity.getStatus()),
                planId
        );
    }

    private static String toFrontendStatus(HospitalWorkspaceStatus status) {
        return status == HospitalWorkspaceStatus.ACTIVE ? "ACTIVE" : "INACTIVE";
    }
}