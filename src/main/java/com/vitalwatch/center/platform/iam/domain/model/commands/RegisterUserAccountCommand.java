package com.vitalwatch.center.platform.iam.domain.model.commands;

import com.vitalwatch.center.platform.iam.domain.model.enums.UserRole;

/**
 * Command used to register a user account inside a hospital workspace.
 */
public record RegisterUserAccountCommand(
        Long hospitalWorkspaceId,
        Long profileId,
        String email,
        UserRole role
) {
}