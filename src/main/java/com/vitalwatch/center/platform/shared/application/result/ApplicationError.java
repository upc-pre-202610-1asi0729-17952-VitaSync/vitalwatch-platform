package com.vitalwatch.center.platform.shared.application.result;

/**
 * Represents an application-level error.
 *
 * @param code    machine-readable error code
 * @param message human-readable error message
 */
public record ApplicationError(
        String code,
        String message
) {
    public static ApplicationError validation(String message) {
        return new ApplicationError("VALIDATION_ERROR", message);
    }

    public static ApplicationError notFound(String resourceName) {
        return new ApplicationError(
                "RESOURCE_NOT_FOUND",
                resourceName + " was not found."
        );
    }

    public static ApplicationError conflict(String message) {
        return new ApplicationError("RESOURCE_CONFLICT", message);
    }

    public static ApplicationError businessRule(String message) {
        return new ApplicationError("BUSINESS_RULE_VIOLATION", message);
    }

    public static ApplicationError unexpected() {
        return new ApplicationError(
                "UNEXPECTED_ERROR",
                "An unexpected error occurred."
        );
    }
}