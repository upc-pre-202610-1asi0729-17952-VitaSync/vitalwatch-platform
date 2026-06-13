package com.vitalwatch.center.platform.iam.interfaces.rest.transform;

import com.vitalwatch.center.platform.iam.domain.model.commands.AssignUserRoleCommand;
import com.vitalwatch.center.platform.iam.interfaces.rest.resources.AssignUserRoleResource;

/**
 * Assembler to convert AssignUserRoleResource into AssignUserRoleCommand.
 */
public final class AssignUserRoleCommandFromResourceAssembler {

    private AssignUserRoleCommandFromResourceAssembler() {
    }

    public static AssignUserRoleCommand toCommandFromResource(Long userAccountId, AssignUserRoleResource resource) {
        return new AssignUserRoleCommand(userAccountId, resource.role());
    }
}