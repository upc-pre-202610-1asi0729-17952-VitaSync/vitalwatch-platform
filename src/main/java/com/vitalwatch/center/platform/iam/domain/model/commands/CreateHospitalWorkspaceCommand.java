package com.vitalwatch.center.platform.iam.domain.model.commands;

/**
 * Command used to register a hospital workspace.
 */
public record CreateHospitalWorkspaceCommand(
        String name,
        String ruc,
        Long administratorProfileId,
        String administratorEmail
) {
}