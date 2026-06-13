package com.vitalwatch.center.platform.iam.interfaces.rest.transform;

import com.vitalwatch.center.platform.iam.domain.model.commands.InviteUserCommand;
import com.vitalwatch.center.platform.iam.interfaces.rest.resources.InviteUserResource;

/**
 * Assembler to convert InviteUserResource into InviteUserCommand.
 */
public final class InviteUserCommandFromResourceAssembler {

    private InviteUserCommandFromResourceAssembler() {
    }

    public static InviteUserCommand toCommandFromResource(InviteUserResource resource) {
        return new InviteUserCommand(
                resource.hospitalWorkspaceId(),
                resource.email(),
                resource.role()
        );
    }
}