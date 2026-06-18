package com.vitalwatch.center.platform.frontendcompat.interfaces.rest.transform;

import com.vitalwatch.center.platform.iam.domain.model.enums.UserRole;

/**
 * Maps backend IAM roles to the role names expected by the Angular frontend.
 */
public final class FrontendRoleMapper {

    private FrontendRoleMapper() {
    }

    public static String toFrontendRole(UserRole role) {
        return switch (role) {
            case HOSPITAL_ADMIN -> "HOSPITAL_ADMIN";
            case MEDICAL_DIRECTOR -> "SUPERVISOR";
            case CLINICAL_SUPERVISOR -> "SUPERVISOR";
            case MEDICAL_STAFF -> "DOCTOR";
        };
    }

    public static UserRole toBackendRole(String role) {
        if (role == null || role.isBlank()) {
            throw new IllegalArgumentException("Role must not be null or blank");
        }

        return switch (role.trim().toUpperCase()) {
            case "HOSPITAL_ADMIN", "ADMIN", "ADMINISTRATOR" -> UserRole.HOSPITAL_ADMIN;
            case "SUPERVISOR", "CLINICAL_SUPERVISOR" -> UserRole.CLINICAL_SUPERVISOR;
            case "DOCTOR", "MEDICAL_STAFF", "STAFF" -> UserRole.MEDICAL_STAFF;
            case "MEDICAL_DIRECTOR" -> UserRole.MEDICAL_DIRECTOR;
            default -> throw new IllegalArgumentException("Unsupported role: " + role);
        };
    }
}