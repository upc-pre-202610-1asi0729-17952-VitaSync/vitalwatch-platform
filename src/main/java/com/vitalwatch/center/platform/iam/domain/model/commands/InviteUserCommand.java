package com.vitalwatch.center.platform.iam.domain.model.commands;

import com.vitalwatch.center.platform.iam.domain.model.enums.UserRole;

/**
 * Command used to invite a user to a hospital workspace.
 */
public record InviteUserCommand(
        Long hospitalWorkspaceId,
        String email,
        UserRole role
) {
}