package com.vitalwatch.center.platform.iam.domain.model.commands;

import com.vitalwatch.center.platform.iam.domain.model.enums.UserRole;

/**
 * Command used to update the institutional role of a user account.
 */
public record AssignUserRoleCommand(
        Long userAccountId,
        UserRole role
) {
}